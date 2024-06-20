package cn.com.goldwind.md4x.business.service;

import cn.com.goldwind.md4x.business.bo.download.DownloadFileVO;
import cn.com.goldwind.md4x.business.bo.upload.uploadFileBO;
import cn.com.goldwind.md4x.util.DateUtil;
import cn.com.goldwind.md4x.util.FileUtil;
import cn.com.goldwind.md4x.util.IoUtil;
import cn.com.goldwind.md4x.util.TreeUtil;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

/**
 * 
 * @Title: S3Service.java
 * @Package cn.com.goldwind.md4x.business.service
 * @description AWS S3服务类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 5, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Service
public class AwsS3Service implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(AwsS3Service.class);

	private AmazonS3 s3;

	@Value("${aws.credentialFlag}")
	private String credentialFlag;

	@Value("${aws.ak}")
	private String accessKey;

	@Value("${aws.sk}")
	private String secretKey;

	@Value("${aws.region}")
	private String region;

	@Override
	public void afterPropertiesSet() throws Exception {
		if ("true".equals(credentialFlag.trim())) {
			if(!isNullOrEmpty(accessKey) && !isNullOrEmpty(secretKey)){
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
			}else {
				s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
			}
		} else {
			s3 = AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		}
	}

	/**
	 * 创建AWS S3桶
	 * 
	 * @param backetName 桶名
	 */
	private void createBucket(String backetName) {
		if (!s3.doesBucketExistV2(backetName)) {
			s3.createBucket(backetName);
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param file       文件
	 * @param filePath   路径，例如：md4x-poc/private/50508/upload
	 * @param bucketName 桶名
	 * @return
	 */
	public boolean uploadFileToS3(MultipartFile file, String filePath, String bucketName) {
		boolean flag = false;
		try (InputStream inputStream = file.getInputStream()) {
			// 创建s3桶
			createBucket(bucketName);
			// 设置大小
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			// 原文件名包括后缀
			String fileName = file.getOriginalFilename();
			PutObjectRequest putObjectRequest = new PutObjectRequest(filePath, fileName, inputStream, metadata);
			PutObjectResult result = s3.putObject(putObjectRequest);
			if (null != result) {
				flag = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 生成S3文件的公共下载URL
	 * 
	 * @param filePath 文件在S3上的路径，例如：md4x-poc/private/50508/upload
	 * @param fileName 文件名，例如：test.txt
	 * @return
	 */
	public String generateDownloadURL(String filePath, String fileName) {
		// 获取一个request
		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(filePath, fileName);
		// 生成公用的URL,通过该URL可以下载该文件
		URL url = s3.generatePresignedUrl(urlRequest);
		return url.toString();
	}

	/**
	 * 重命名文件
	 * 
	 * @param bucketName 桶名
	 * @param sourceKey  源文件名,例如: private/50508/upload/test2.txt
	 * @param targetKey  目标文件名,例如: private/50508/upload/test4.txt
	 * @return
	 */
	public Boolean copyFile(String bucketName, String sourceKey, String targetKey) {
		// 1.先复制一份新文件
		s3.copyObject(bucketName, sourceKey, bucketName, targetKey);
		// 2.再删除之前的文件
		boolean flag = deleteFileFromS3(bucketName, sourceKey);
		if (flag) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除S3桶里的文件
	 * 
	 * @param bucketName 桶名,例如：md4x-poc
	 * @param key        要删除的文件,例如：private/50508/upload/meeting.txt
	 * @return
	 */
	public Boolean deleteFileFromS3(String bucketName, String key) {
		try {
			s3.deleteObject(bucketName, key);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 列出AWS S3桶里面的对象
	 * 
	 * @param bucketName 桶名，例如:"md4x-poc"
	 * @param prefix     前缀，例如:private/50508
	 * @return
	 */
	public List<uploadFileBO> listS3FolderInfo(String bucketName, String prefix) {
		List<uploadFileBO> uploadFileList = new ArrayList<uploadFileBO>();
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix));
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			uploadFileBO uploadFile = new uploadFileBO();
			// 德阳：设置最近更新时间
			uploadFile.setFileCreateTime(DateUtil.date2StringPattern(objectSummary.getLastModified(), "yyyy-MM-dd HH:mm:ss"));
			// 永刚：获取s3文件key
			String key = objectSummary.getKey();
			// 永刚更新德阳的代码：根据key获取文件
			String file = key.substring(objectSummary.getKey().lastIndexOf("/") + 1, objectSummary.getKey().length());
			// 永刚：设置key
			uploadFile.setS3Key(key);
			// 德阳：过滤没有后缀的文件
			if (file.indexOf(".") >= 0) {
				uploadFile.setFileName(file.substring(0, file.lastIndexOf(".")));
				uploadFile.setFileType(file.substring(file.lastIndexOf("."), file.length()));
			} else {
				uploadFile.setFileName(file);
				uploadFile.setFileType("");
			}
			uploadFileList.add(uploadFile);
		}
		return uploadFileList;
	}

	/**
	 * 没用到
	 * 
	 * @param folderName 文件夹名，例如：md4x-poc/private/50508/upload/
	 * @param bucketName 桶名
	 */
	@Deprecated
	public void createFolder(String folderName, String bucketName) {
		// 创建S3桶
		createBucket(bucketName);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		InputStream inputStream = new ByteArrayInputStream(new byte[0]);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, inputStream, metadata);
		s3.putObject(putObjectRequest);
	}

	/**
	 * 下载文件(没用到)
	 * 
	 * @param bucketName 桶名,例如：md4x-poc
	 * @param key        要下载的文件
	 * @return
	 */
	@Deprecated
	public String downS3File(String bucketName, String key) {
		String str = "";
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			if (null != object) {
				inputStreamReader = new InputStreamReader(object.getObjectContent());
				bufferedReader = new BufferedReader(inputStreamReader);
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				str = sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoUtil.close(bufferedReader, inputStreamReader);
		}
		return str;
	}

	/**
	 * 修改文件的内容(没用到)
	 * 
	 * @param bucketName  桶名
	 * @param filePath    文件路径, 例如：md4x-poc/private/50508/upload
	 * @param fileType    文件类型
	 * @param fileContent 文件内容
	 * @return
	 */
	@Deprecated
	public String updateFileToS3(String bucketName, String filePath, String fileType, String fileContent) {
		try {
			// 创建s3桶
			createBucket(bucketName);
			// 设置大小
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(fileContent.getBytes("utf-8").length);
			// 原文件名包括后缀
			InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
			PutObjectRequest putObjectRequest = new PutObjectRequest(filePath, fileType, inputStream, metadata);
			s3.putObject(putObjectRequest);
			// 获取一个request
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(filePath, fileType);
			// 生成公用的url
			URL url = s3.generatePresignedUrl(urlRequest);
			return url.toString();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @Author yaleiwang
	 * @Description 以树状结构列出AWS S3桶prefix下文件
	 * @Date 2020-9-21 0:54
	 * @param bucketName
	 * @param prefix
	 * @return java.util.List<cn.com.goldwind.md4x.business.bo.download.downloadFileBO>
	 **/
	public List<DownloadFileVO> listDownloadFileInfo(String bucketName, String prefix) {
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withMaxKeys(100));
		List<DownloadFileVO> wfidList=new ArrayList<>();
		DecimalFormat df = new DecimalFormat("######0.0");
		try {
			while (true) {
				List<S3ObjectSummary> summaries = objectListing.getObjectSummaries();
				for (S3ObjectSummary summary : summaries) {
					// 获取最近修改时间
					String lastModified = DateUtil.date2StringPattern(summary.getLastModified(), "yyyy-MM-dd HH:mm:ss");
					// 获取s3文件key
					String key = summary.getKey();

					//路径分隔
					String[] pathSplit = key.split("/");

					for (int i = 4; i < pathSplit.length; i++) {
						DownloadFileVO downloadFile = new DownloadFileVO();
						String fileName = pathSplit[i];
						downloadFile.setFileName(fileName);
						if(!wfidList.contains(downloadFile)){
							String pid = i > 4 ? pathSplit[i - 1] : "";
							downloadFile.setFileLastModified(lastModified);
							downloadFile.setPid(pid);
							int index = getIndex(key, "/",i+1);
							String tmpKey = index == 0 ? key : key.substring(0, index);
							downloadFile.setFileKey(tmpKey);

							if(!tmpKey.endsWith("/")){
								// 获取文件大小
								float fileSize = (float) (summary.getSize()) / 1024;

								if (fileSize > 1024) {
									fileSize = fileSize / 1024;
									downloadFile.setFileSize(df.format(fileSize) + " MB");
								} else {
									downloadFile.setFileSize(df.format(fileSize) + " KB");
								}

								downloadFile.setFolder(false);
							}else {
								downloadFile.setFolder(true);
							}

							wfidList.add(downloadFile);
						}
					}

				}
				if (objectListing.isTruncated()) {
					objectListing = s3.listNextBatchOfObjects(objectListing);
				} else {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		List<DownloadFileVO> result = TreeUtil.getTree(wfidList);
		return result;
	}
	
	/**
	 * @Author yaleiwang
	 * @Description 获取prefix所有的文件或文件夹
	 * @Date 2020-10-19 10:15
	 * @param bucketName bucket name
	 * @param prefix prefix
	 * @return java.util.List<cn.com.goldwind.md4x.business.bo.download.DownloadFileVO>
	 **/
	public List<DownloadFileVO> listDownloadFileInfoV2(String bucketName, String prefix) {
		String marker = null;
		List<DownloadFileVO> downloadFileList = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("######0.0");
		while (true) {
			try {
				ObjectListing res = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withDelimiter("/").withMaxKeys(10).withMarker(marker));

				for (String p : res.getCommonPrefixes()) {
					DownloadFileVO downloadFile = new DownloadFileVO();
					downloadFile.setFileKey(p);
					downloadFile.setFolder(true);
					p = p.substring(0, p.length() - 1);
					downloadFile.setFileName(p.substring(p.lastIndexOf("/") + 1));
					downloadFileList.add(downloadFile);
				}

				for (S3ObjectSummary obj : res.getObjectSummaries()) {
					DownloadFileVO downloadFile = new DownloadFileVO();
					downloadFile.setFileLastModified(DateUtil.date2StringPattern(obj.getLastModified(), "yyyy-MM-dd HH:mm:ss"));
					String key = obj.getKey();
					if(!key.endsWith("/")){
						downloadFile.setFileKey(key);
						downloadFile.setFolder(false);
						downloadFile.setFileName(key.substring(key.lastIndexOf("/") + 1));

						float fileSize = (float) (obj.getSize()) / 1024;
						if (fileSize > 1024) {
							fileSize = fileSize / 1024;
							downloadFile.setFileSize(df.format(fileSize) + " MB");
						} else if(fileSize > 0) {
							downloadFile.setFileSize(df.format(fileSize) + " KB");
						}

						downloadFileList.add(downloadFile);
					}
				}

				marker = res.getNextMarker();

				if (!res.isTruncated()) {
					break;
				}
			} catch (AmazonClientException ex) {
				log.error(ex.getMessage());
			}
		}

		return downloadFileList;
	}

	/**
	 * @Author yaleiwang
	 * @Description 截取第n个字符之前的字符串
	 * @Date 2020-10-15 15:22
	 * @param key key
	 * @param splitStr 分隔字符
	 * @param count 第几个字符
	 * @return int
	 **/
	private int getIndex(String key, String splitStr, int count){
		int index = -1;
		for(int i = 0; i < count; i++){
			index=key.indexOf(splitStr,(index+1));
		}

		return index+1;
	}

	/**
	 * @Author yaleiwang
	 * @Description 从s3上下载文件并压缩
	 * @Date 2020-10-30 21:18
	 * @param bucketName bucket
	 * @param projectId project id
	 * @param projectName project name
	 * @param keys key list
	 * @param downloadDate date
	 * @param s3Path s3 path
	 * @param request request
	 * @return java.math.BigDecimal
	 **/
	public BigDecimal downloadS3File(String bucketName, Integer projectId, String projectName, List<String> keys, Date downloadDate, String s3Path, HttpServletResponse response, HttpServletRequest request) throws Exception {
		FileOutputStream zipSourceStream = null;
		File zipFile = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String zipFilePath = String.format("%s/download/%s/%s_%s.zip", System.getProperty("user.dir"),projectId, projectName, sdf.format(downloadDate));
			zipFile = new File(zipFilePath);
			File parentDir = zipFile.getParentFile();

			if (!parentDir.exists()){
				parentDir.mkdirs();
			}

			if(!zipFile.exists()){
				zipFile.createNewFile();
			}
			else{
				//如果压缩包已经存在则删除后重新打包压缩
				zipFile.delete();
			}
			// 创建文件输出流
			zipSourceStream  = new FileOutputStream(zipFile);
			// 打包的方法用到ZipOutputStream这样一个输出流,所以这里把输出流转换一下*/
			ZipOutputStream zipOutStream = new ZipOutputStream(zipSourceStream);
			for(String key :keys){
				List<String> subKeys = getKeysByPrefix(bucketName,key);
				for(String subkey :subKeys){
					String filePath = subkey.replace(s3Path,"");
					S3Object s3Object = s3.getObject(bucketName, subkey);
					S3ObjectInputStream inputStream = s3Object.getObjectContent();
					zipInputStream(inputStream,filePath,zipOutStream);
				}
			}
			//先关闭zip流才能下载
			zipOutStream.flush();
			zipOutStream.close();

			//压缩包大小
			float fileSize = (float) (zipFile.length()) / 1024 / 1024;
			DecimalFormat decimalFormat=new DecimalFormat("##0.00");
			BigDecimal sizeDecimal= new BigDecimal(decimalFormat.format(fileSize));

			// 把该压缩文件通过流输出给客户端后删除该压缩文件 false-未处理
			FileUtil.downloadV2(zipFile, response, request);
			return sizeDecimal;

		}finally {
			IoUtil.close(zipSourceStream);
			if(zipFile != null){
				zipFile.delete();
			}
		}
	}


	/**
	 * @Author yaleiwang
	 * @Description s3 object inputStream 输出到 zip outputSteam
	 * @Date 2020-9-23 13:46
	 * @param inputStream s3 object inputStream
	 * @param filePath key 相对zip文件的相对路径
	 * @param ouputStream zip outputSteam
	 * @return void
	 **/
	public void zipInputStream(S3ObjectInputStream inputStream,String filePath, ZipOutputStream ouputStream){
		BufferedInputStream bins = null;
		try{
			bins = new BufferedInputStream(inputStream, 1024);
			//org.apache.tools.zip.ZipEntry
			ZipEntry entry = new ZipEntry(filePath);
			ouputStream.putNextEntry(entry);
			// 向压缩文件中输出数据
			int nNumber;
			byte[] buffer = new byte[1024];
			while ((nNumber = bins.read(buffer)) != -1) {
				ouputStream.write(buffer, 0, nNumber);
			}

		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			//关闭创建的流对象
			IoUtil.close(bins, inputStream);
		}
	}
	
	/**
	 * @Author yaleiwang
	 * @Description 获取prefix下的所有文件
	 * @Date 2020-9-24 16:06
	 * @param bucketName 桶名
	 * @param prefix 前缀
	 * @return java.util.List<java.lang.String>
	 **/
	private List<String> getKeysByPrefix(String bucketName, String prefix){
		List<String> keys = new ArrayList<>();
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withMaxKeys(20));
		while (true) {
			List<S3ObjectSummary> summaries = objectListing.getObjectSummaries();
			for (S3ObjectSummary summary : summaries) {
				String key = summary.getKey();
				keys.add(key);
			}
			if (objectListing.isTruncated()) {
				objectListing = s3.listNextBatchOfObjects(objectListing);
			} else {
				break;
			}
		}
		return keys;
	}

	/**
	 * @Author yaleiwang
	 * @Description 删除keys
	 * @Date 2020-10-30 22:52
	 * @param bucketName
	 * @param keys
	 * @return int
	 **/
	public int deleteS3Files(String bucketName, List<String> keys){
		int deleteCount = 0;
		for(String prefix :keys){
			ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withMaxKeys(20));
			while (true) {
				List<S3ObjectSummary> summaries = objectListing.getObjectSummaries();
				for (S3ObjectSummary summary : summaries) {
					String key = summary.getKey();
					s3.deleteObject(bucketName, key);
					deleteCount++;
				}
				if (objectListing.isTruncated()) {
					objectListing = s3.listNextBatchOfObjects(objectListing);
				} else {
					break;
				}
			}
//			ListObjectsRequest listObjectsRequest = ListObjectsRequest
//					.bucket(bucket).build();
			prefix = prefix.substring(0, prefix.length()-1);
			s3.deleteObject(bucketName, prefix);
		}
		return deleteCount;
	}

//	private List<String> listKeysInDirectory(String bucket, String prefix){
//		ListObjectsV2Result listRes  = s3.listObjectsV2(new ListObjectsV2Request().withBucketName(bucket).withPrefix(prefix).withMaxKeys(1));
//		List<String> keyList = new ArrayList<>();
//		for(S3ObjectSummary key :listRes.getObjectSummaries()){
//
//		}
//
//
//		final String flolder = prefix;
//		listRes.
//		listRes.contents().stream()
//				.forEach(content -> {
//					if (!flolder.equals(content.key())) {
//						keyList.add(content.key());
//					}
//				});
//		return keyList;
//
//	}

}
