/**   
* @Title: TWfinfoServiceImpl.java 
* @Package cn.com.goldwind.md4x.business.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 15, 2020 8:33:39 AM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.goldwind.md4x.business.bo.zeppelin.ZeppelinNotebook;
import cn.com.goldwind.md4x.business.bo.zeppelin.ZeppelinParagraph;
import cn.com.goldwind.md4x.business.bo.zeppelin.ZeppelinParagraphConfig;
import cn.com.goldwind.md4x.business.entity.datamart.ExparamDatamart;
import cn.com.goldwind.md4x.business.entity.datamart.S3UploadfileIndex;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject;
import cn.com.goldwind.md4x.business.service.datamart.IExparamDatamartService;
import cn.com.goldwind.md4x.business.service.datamart.S3UploadfileIndexService;
import cn.com.goldwind.md4x.business.service.zeppelin.ZeppelinInstanceService;
import cn.com.goldwind.md4x.business.service.zeppelin.ZeppelinProjectService;
import cn.com.goldwind.md4x.util.http.HttpAPIService;
import cn.com.goldwind.md4x.util.http.HttpResult;



@Service
public class ZeppelinService  {
	private static final Logger log = LoggerFactory.getLogger(ZeppelinService.class);

	@Autowired
	HttpAPIService httpAPIService;
	
	@Autowired
	private ZeppelinInstanceService zeppelinService;
	
	@Autowired
	private ZeppelinInstanceService zeppelinInstanceService;
	
	@Autowired
	ZeppelinProjectService zeppelinProjectService;
	
	@Autowired
	IExparamDatamartService exparamDatamartService;
	
	@Autowired
	S3UploadfileIndexService s3UploadfileIndexService;
	
	@Value("${zeppelin.url}")
	private String zeppelinUrl;
	
	@Value("${zeppelin.env}")
	private String zeppelinEnv;
	
	@Value("${aws.ecs.cookieDomain}")
	private String cookieDomain;
	
	
	public String redirectZeppelin(HttpServletResponse response, String userName, String password,String projectId) {
		String notebookId = null;
		String url = null;
		if("prod".equals(zeppelinEnv)) {
			SysZeppelin zeppelin = zeppelinInstanceService.selectByUserName(userName);
			String zeppelinContextUrl = zeppelinUrl+"/"+zeppelin.getInstanceName();//生产环境共,多用户共用一个ELB,通过instanceName区分
			notebookId = loginZeppelin(response,zeppelinContextUrl, userName, password,projectId);
			log.info(String.format("zeppelinContextUrl:%s,userName:%s,password:%s", zeppelinContextUrl,userName,password));
			url = zeppelinContextUrl + "/#/notebook/" + notebookId;
			log.info(String.format("url:%s", url));
		}else {
			notebookId = loginZeppelin(response,zeppelinUrl, userName, password,projectId);
			url = zeppelinUrl+"/#/notebook/" + notebookId;
			log.info(String.format("url:%s", url));
		}
		return url;
	}
	
	/**
	 * 模拟登录到zeppelin
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 */
	public String loginZeppelin(HttpServletResponse response,String zeppelinUrl,String userName, String password,String projectId) {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionTimeToLive(10000, TimeUnit.MILLISECONDS).build();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userName", userName));
		HttpPost httpPost = new HttpPost(zeppelinUrl.trim()+"/api/login");
		log.info(String.format("httpPost:%s", zeppelinUrl.trim()+"/api/login"));
		httpPost.setHeader(new BasicHeader("Content-type", "application/x-www-form-urlencoded"));
		if ("prod".equals(zeppelinEnv)) {
			SysZeppelin sysZeppelin = zeppelinService.selectByUserName(userName);
			list.add(new BasicNameValuePair("password", sysZeppelin.getUserPwd()));
		} else {
			list.add(new BasicNameValuePair("password", password));
		}
		
		
		HttpResponse loginResponse = null;
		int statusCode = 0;
		HashMap<String, String> cookies = new HashMap<String, String>();
		String notebookId = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
			loginResponse = httpClient.execute(httpPost);
			statusCode = loginResponse.getStatusLine().getStatusCode();
			Header[] headers = loginResponse.getHeaders("Set-Cookie");
			log.info(String.format("header:%s", headers.toString()));
			for (Header header : headers) {
				if (header.getValue().contains("md4xcookie")) {
					String token = header.getValue().substring(header.getValue().indexOf("=") + 1,
							header.getValue().indexOf(';'));
				
					cookies.put("md4xcookie", token);//cookie
				}
			}
			cookies.put("statusCode", statusCode+"");//状态码
			
			//set cookie 给请求方(浏览器)
			Cookie cookie = new Cookie("md4xcookie",cookies.get("md4xcookie"));
			cookie.setDomain(cookieDomain);
			cookie.setPath("/");
			cookie.setMaxAge(3600);//60分钟，3600秒
			response.addCookie(cookie);
			
			//封装要携带的cookie
			BasicCookieStore cookieStore = new BasicCookieStore();
			//httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
			BasicClientCookie tokenCookie = new BasicClientCookie("md4xcookie",cookies.get("md4xcookie"));
			tokenCookie.setPath("/");
			tokenCookie.setExpiryDate(new Date(System.currentTimeMillis() + 60 * 60 * 1000));//超时时间 60分钟
			tokenCookie.setDomain(cookieDomain);
			cookieStore.addCookie(tokenCookie);
			
			//通过项目id获取项目信息
			SysZeppelinProject szp = zeppelinProjectService.getProjectById(Integer.parseInt(projectId));
			
			//创建一个notebook
			String creatNotebookUrl = zeppelinUrl+"/api/notebook";
			String notebookName = "/"+userName+"/"+szp.getProjectName();
			ZeppelinNotebook zeppelinNotebook = new ZeppelinNotebook();
			zeppelinNotebook.setName(notebookName);//笔记名称
			List<ZeppelinParagraph> zeppelinParagraphs = new ArrayList<ZeppelinParagraph>();
			
			//判断笔记是否已经存在,如存在，直接返回notebookId
			notebookId = isExistNotebook(creatNotebookUrl,notebookName,httpClient);
			if(notebookId != null && !"".equals(notebookId)) {
				//return notebookId;
			}else {
				//1.帮助AthenaHelper
				ZeppelinParagraphConfig config = new ZeppelinParagraphConfig();
				config.setEditorHide(false);
				config.setEnabled(true);
				String title = "athena_helper可用方法说明(请移除注释并执行这段脚本!)";
				String text = readTextFromTemplate("zeppelinTemplate/AthenaHelper.txt");
				ZeppelinParagraph zeppelinParagraph1 = createParagraph(config,title,text);
				zeppelinParagraphs.add(zeppelinParagraph1);
				
				//2.项目数据集信息
				config = new ZeppelinParagraphConfig();
				config.setEditorHide(true);
				config.setEnabled(true);
				title = "项目数据集信息";
				text = readTextFromTemplate("zeppelinTemplate/projectDatasetInfo.txt");
				StringBuffer s = new StringBuffer(text);
				
				String projectName = szp.getProjectName();
				Map<String,Object> datasets = (Map<String,Object>)szp.getDatasetId();
				for(String key:datasets.keySet()) {
					String ids = JSONArray.toJSONString(datasets.get(key));
					List<Integer> listIds = JSONObject.parseArray(ids, Integer.class);
					for(Integer id : listIds) {
						if(key.equals("file")) {
							S3UploadfileIndex fileIndex= s3UploadfileIndexService.getById(id);
							s.append("\n");
							s.append("|"+"上传文件"+"|"+key+"|"+"无"+"|"+fileIndex.getS3Key());
						}else {//public 或 private 数据集
							ExparamDatamart datamart = exparamDatamartService.getById(id);
							s.append("\n");
							s.append("|"+datamart.getDatamartName()+"|"+key+"|"+datamart.getAthenaDb()+"|"+datamart.getAthenaTable());
						}
					}
				}
				
				
			
				ZeppelinParagraph zeppelinParagraph2 = createParagraph(config,title,s.toString());
				zeppelinParagraphs.add(zeppelinParagraph2);
				
				
				/*
				//2.个人数据集
				ZeppelinParagraphConfig config2 = new ZeppelinParagraphConfig();
				String title2 = "个人数据集";
				String text2 = readTextFromTemplate("zeppelinTemplate/personalDataSet.txt");
				ZeppelinParagraph zeppelinParagraph2 = createParagraph(config2,title2,text2.replace("$USERNAME$", userName));
				zeppelinParagraphs.add(zeppelinParagraph2);
				
				//3.数据集信息
				ZeppelinParagraphConfig config3 = new ZeppelinParagraphConfig();
				String title3 = "数据集信息";
				String text3 = readTextFromTemplate("zeppelinTemplate/datasetInfo.txt");
				ZeppelinParagraph zeppelinParagraph3 = createParagraph(config3,title3,text3);
				zeppelinParagraphs.add(zeppelinParagraph3);
				
				//4.数据完整度
				ZeppelinParagraphConfig config4 = new ZeppelinParagraphConfig();
				String title4 = "数据完整度";
				String text4 = readTextFromTemplate("zeppelinTemplate/integrity.txt");
				ZeppelinParagraph zeppelinParagraph4 = createParagraph(config4,title4,text4);
				zeppelinParagraphs.add(zeppelinParagraph4);
				
				//5.查询数据集
				ZeppelinParagraphConfig config5 = new ZeppelinParagraphConfig();
				String title5= "数据集查询";
				String text5 = readTextFromTemplate("zeppelinTemplate/queryDataset.txt");
				ZeppelinParagraph zeppelinParagraph5 = createParagraph(config5,title5,text5);
				zeppelinParagraphs.add(zeppelinParagraph5);
				*/
				
				
				
				//创建笔记
				zeppelinNotebook.setZeppelinParagraphs(zeppelinParagraphs);//paragraph
				notebookId = createNotebook(creatNotebookUrl,zeppelinNotebook,httpClient);//创建笔记	
				
				//运行一个笔记的所有paragraph
				String notebookUrl = zeppelinUrl+"/api/notebook/job/"+notebookId;
				runParagraphByNotebookId(notebookUrl,httpClient);
			}
			
			/*
			//已知笔记创建paragraph
			ZeppelinParagraphConfig config6 = new ZeppelinParagraphConfig();
			config6.setEditorHide(true);
			String title6= "数据集查询";
			String text6 = readTextFromTemplate("zeppelinTemplate/queryDataset.txt");
			ZeppelinParagraph zeppelinParagraph6 = createParagraph(config6,title6,text6);
			String paragraphId = createParagraph(creatNotebookUrl,notebookId,zeppelinParagraph6,httpClient);
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return notebookId;
	}
	
	
	/**
	 * 从本地模板中读取text
	 * @param templatePath
	 * @return
	 */
	private String readTextFromTemplate(String templatePath) {
		ClassPathResource classPathResource = new ClassPathResource(templatePath);
		InputStream inputStream = null;
		try {
			inputStream = classPathResource.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String text = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));
		return text;
	}
	
	/**
	 * 创建一个paragraph
	 * @return
	 */
	private ZeppelinParagraph createParagraph(ZeppelinParagraphConfig config,String title,String text) {
		ZeppelinParagraph zeppelinParagraph = new ZeppelinParagraph();
		zeppelinParagraph.setConfig(config);
		zeppelinParagraph.setTitle(title);
		zeppelinParagraph.setText(text);
		return zeppelinParagraph;
	}
	
	private String isExistNotebook(String creatNotebookUrl,String notebookName,CloseableHttpClient httpClient) {
		String notebookId = "";
		//判断要创建的笔记是否存在
		Map<String,String> maps = getAllNotebooks(creatNotebookUrl,httpClient);//所有notebook集合
		notebookId = maps.get(notebookName);
	    return notebookId;
	}
	
	/**
	 * 创建笔记
	 * @param creatNotebookUrl
	 * @param zeppelinNotebook
	 * @param httpClient
	 * @return
	 */
	private String createNotebook(String creatNotebookUrl,ZeppelinNotebook zeppelinNotebook,CloseableHttpClient httpClient) {
		String notebookId = "";
		/*
		//判断要创建的笔记是否存在
		Map<String,String> maps = getAllNotebooks(creatNotebookUrl,httpClient);//所有notebook集合
		notebookId = maps.get(zeppelinNotebook.getName());
	    if(notebookId != null)
	    	return notebookId;
	    	*/
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("name", zeppelinNotebook.getName());
		map.put("paragraphs", zeppelinNotebook.getZeppelinParagraphs());
		HttpPost httpPost = new HttpPost(creatNotebookUrl.trim());
		httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
		String  jsonString= JSON.toJSONString(map);
	    StringEntity entity = new StringEntity(jsonString,"utf-8");
	    entity.setContentEncoding("UTF-8");
	    entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse loginResponse;
		//int statusCode = loginResponse1.getStatusLine().getStatusCode();
		String respone = null;
		try {
			loginResponse = httpClient.execute(httpPost);
			respone = EntityUtils.toString(loginResponse.getEntity(), "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		notebookId = JSONObject.parseObject(respone).getString("body");
		return notebookId;
	}
	
	/**
	 * 在已经创建的笔记中创建paragraph
	 * @param creatNotebookUrl
	 * @param notebookId
	 * @param zeppelinParagraph
	 * @param httpClient
	 * @return
	 */
	private String createParagraph(String creatNotebookUrl,String notebookId,ZeppelinParagraph zeppelinParagraph,CloseableHttpClient httpClient) {
		String paragraphId = "";
		/*
		//判断要创建的笔记是否存在
		Map<String,String> maps = getAllNotebooks(creatNotebookUrl,httpClient);//所有notebook集合
		notebookId = maps.get(zeppelinNotebook.getName());
	    if(notebookId != null)
	    	return notebookId;
	    	*/
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("title", zeppelinParagraph.getTitle());
		map.put("text", zeppelinParagraph.getText());
		map.put("config", zeppelinParagraph.getConfig());
		HttpPost httpPost = new HttpPost(creatNotebookUrl.trim()+"/"+notebookId+"/paragraph");
		httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
		String  jsonString= JSON.toJSONString(map);
	    StringEntity entity = new StringEntity(jsonString,"utf-8");
	    entity.setContentEncoding("UTF-8");
	    entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse loginResponse;

		String respone = null;
		try {
			loginResponse = httpClient.execute(httpPost);
			respone = EntityUtils.toString(loginResponse.getEntity(), "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		paragraphId = JSONObject.parseObject(respone).getString("body");
		return paragraphId;
	}
	
	
	/**
	 * 查询所有笔记
	 * @param getNotebookUrl
	 * @param httpClient
	 * @return
	 */
	private Map<String,String> getAllNotebooks(String getNotebookUrl,CloseableHttpClient httpClient){
		HttpGet httpGet = new HttpGet(getNotebookUrl);
		//httpGet.addHeader("Cookie", cookieStore.toString());
		CloseableHttpResponse resp = null;
		HttpResult httpResult = null;
		try {
			resp = httpClient.execute(httpGet);
			httpResult = new HttpResult(resp.getStatusLine().getStatusCode(),EntityUtils.toString(resp.getEntity(), "UTF-8"));
		}catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	     Map<String,Object> maps = (Map<String,Object>)JSON.parse(httpResult.getBody()); 
	     Map<String,String> mapResult = new HashMap<String,String>();
	     if(maps != null) {
	    	 List<Map<String,String>> lists = (List<Map<String,String>>)maps.get("body");
	    	 for(Map<String,String> map : lists) {
	    		 mapResult.put(map.get("path"),map.get("id"));//key:notebook path  value: notebook id
	    	 } 
	     }
	     return mapResult;
		
	}
	
	/**
	 * 运行一个笔记的所有paragraph
	 * @param creatNotebookUrl
	 * @param zeppelinNotebook
	 * @param httpClient
	 * @return
	 */
	private String runParagraphByNotebookId(String notebookUrl,CloseableHttpClient httpClient) {
		String message = "";
		HttpPost httpPost = new HttpPost(notebookUrl.trim());
		httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
        HttpResponse loginResponse;
		//int statusCode = loginResponse1.getStatusLine().getStatusCode();
		String respone = null;
		try {
			loginResponse = httpClient.execute(httpPost);
			respone = EntityUtils.toString(loginResponse.getEntity(), "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = JSONObject.parseObject(respone).getString("message");
		return message;
	}
	
	public String getAllNotebooks(HttpServletRequest request) {
		String url = "http://z.goldwind.com.cn:8080/api/notebook";
		String  httpResult = null;
		try {
			httpResult = httpAPIService.doGet(url);
			System.out.println("body---"+httpResult);
		}catch(Exception e) {
		}
		return httpResult;
	}
	
}
