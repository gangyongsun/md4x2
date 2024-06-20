package cn.com.goldwind.md4x.shiro.controller;

import cn.com.goldwind.md4x.business.bo.*;
import cn.com.goldwind.md4x.business.bo.download.DownloadFileVO;
import cn.com.goldwind.md4x.business.bo.download.DownloadKeyBO;
import cn.com.goldwind.md4x.business.entity.zeppelin.*;
import cn.com.goldwind.md4x.business.service.AwsS3Service;
import cn.com.goldwind.md4x.business.service.ZeppelinService;
import cn.com.goldwind.md4x.business.service.zeppelin.*;
import cn.com.goldwind.md4x.config.aop.OperationLogDetail;
import cn.com.goldwind.md4x.config.aop.OperationType;
import cn.com.goldwind.md4x.config.aop.OperationUnit;
import cn.com.goldwind.md4x.mybatis.Page;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Api(value = "zeppelin应用接口", tags = { "zeppelin应用接口" })
@RestController
public class ZeppelinController extends BaseController {
	
	@Autowired
	ZeppelinService zeppelinService;

	@Autowired
	private ZeppelinImageService imageService;

	@Autowired
	private ZeppelinInstanceService zeppelinInstanceService;

	@Autowired
	private ZeppelinResourceService zeppelinResourceService;

	@Autowired
	private ZeppelinProjectService zeppelinProjectService;

	@Autowired
	private ZeppelinLogsService zeppelinLogsService;
	
	@Autowired
	AwsEcsService AwsEcsService;

	@Autowired
	private AwsS3Service s3Service;

	@Autowired
	private ResultDownloadService resultDownloadService;

	@Value("${aws.s3bucketName}")
	private String bucketName;
	
	@ApiOperation(value = "跳转登录zeppelin", notes = "跳转登录zeppelin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userName", value = "zeppelin用户名", required = true, dataType = "String"),
		@ApiImplicitParam(name = "password", value = "zeppelin密码", required = true, dataType = "String") 
		})
	
	@RequestMapping(value = "/data/zeppelin/login", method = RequestMethod.POST)
	@OperationLogDetail(detail = "运行zeppelin",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
	public ResultInfoDTO<Object> postLoginZeppelin(HttpServletResponse response, HttpServletRequest request,@RequestBody ZeppelinVO zeppelinVO) {
		String userName = zeppelinVO.getUserName();
		String password = zeppelinVO.getPassword();
		String projectId = zeppelinVO.getProjectId();
		String ret = zeppelinService.redirectZeppelin(response,userName, password,projectId);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		if(ret != null) {
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("登录zeppelin成功!");
			res.setData(ret);  
		}else {
			res.setCode(ResultInfoDTO.ERROR);
			res.setMessage("登录zeppelin的用户名或密码不正确!");
			res.setData(ret+"");
		}
	
		return res;
	}
	
	/**
	 * 跳转到zeppelin平台
	 * @param response
	 * @param request
	 * @return
	 */
	/*
	@RequestMapping(value = "/data/zeppelin/login", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getLoginZeppelin(HttpServletResponse response, HttpServletRequest request,String userName,String password) {
		String  ret = zeppelinService.loginZeppelin(response, userName, password);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		if(ret != null) {
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("登录zeppelin成功!");
			res.setData(ret);  
		}else {
			res.setCode(ResultInfoDTO.ERROR);
			res.setMessage("登录zeppelin的用户名或密码不正确!");
			res.setData(ret+"");
		}
		return res;
	}*/
	
	
	@ApiOperation(value = "获取所有zeppelin notebooks", notes = "获取所有zeppelin notebooks")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/data/zeppelin/notebooks", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getAllNotebooks(HttpServletResponse response, HttpServletRequest request) {
		String  ret = zeppelinService.getAllNotebooks(request);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(ret);  
		return res;
	}
	
	
	@ApiOperation(value = "zeppelin实例启动", notes = "zeppelin实例启动")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userName", value = "用户OA", required = true, dataType = "String"),
			@ApiImplicitParam(name = "taskFlavor", value = "任务实例大小", required = true, dataType = "String"),
			@ApiImplicitParam(name = "imageId", value = "镜像ID", required = true, dataType = "Integer")
	})
	@RequestMapping(value = "/data/zeppelin/create", method = RequestMethod.POST)
	@OperationLogDetail(detail = "启动zeppelin实例",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.INSERT)
	public ResultInfoDTO<Object> getZeppelinDocker(HttpServletResponse response, HttpServletRequest request,@RequestBody ZeppelinCreateVO zeppelinCreateVO) {
		String userName = zeppelinCreateVO.getUserName();
		String taskFlavor = zeppelinCreateVO.getTaskFlavor();
		Integer imageId = zeppelinCreateVO.getImageId();

		String zeppelinInstanceID = "md4x-zeppelin-"+userName;

		//1.判断是否可以创建
		boolean instanceAvailable = AwsEcsService.checkAvailable(userName);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		if(instanceAvailable){
			//2.查询resource_id
			SysZeppelin zeppelinEntity = new SysZeppelin();
			zeppelinEntity.setUserName(userName);
			zeppelinEntity.setInstanceName(zeppelinInstanceID);
			zeppelinEntity.setTaskFlavor(taskFlavor);
			zeppelinEntity.setImageId(imageId);

			zeppelinEntity.setInstanceStatus("PROVISIONING");

			zeppelinInstanceService.insertZeppelin(zeppelinEntity);

			//3.任务定义
			String taskDefinitionArn = AwsEcsService.prepareZeppelinInstanceTaskDefinition(zeppelinEntity);

			if(taskDefinitionArn==null){
                zeppelinEntity.setInstanceStatus("DELETED");
                zeppelinInstanceService.insertZeppelin(zeppelinEntity);
            }

			//4.启动zeppelin server docker instance
			String serviceArn = AwsEcsService.prepareZeppelinInstanceService(zeppelinEntity, taskDefinitionArn);

			res.setCode(ResultInfoDTO.OK);
			res.setMessage("ok");
			res.setData(taskDefinitionArn+ "," +serviceArn);
			return res;
		}else{
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("service %s exist,please stop service first!",zeppelinInstanceID));
			return res;
		}
	}

	@ApiOperation(value = "delete zeppelin service", notes = "删除zeppelin服务")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "userName", value = "用户OA", required = true, dataType = "String")
	@RequestMapping(value = "/data/zeppelin/delete", method = RequestMethod.DELETE)
	@OperationLogDetail(detail = "销毁zeppelin实例",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
	public ResultInfoDTO<Object> deleteZeppelinDocker(HttpServletResponse response, HttpServletRequest request, String userName) {
		String zeppelinInstanceID = "md4x-zeppelin-"+userName;

		//1.判断service是否存在
		boolean serviceExists = AwsEcsService.zeppelinServiceExists(zeppelinInstanceID);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();

		if(serviceExists){
			try{
				boolean delResult = AwsEcsService.deleteService(zeppelinInstanceID);

				if(delResult){
					res.setCode(ResultInfoDTO.OK);
					res.setMessage("ok");
					return res;
				}else {
					res.setCode(ResultInfoDTO.FAILED);
					res.setMessage("delete failed!");
					res.setData(zeppelinInstanceID);
					return res;
				}
			}catch (Exception e){
				e.printStackTrace();
				res.setCode(ResultInfoDTO.FAILED);
				res.setMessage(String.format("删除失败！ERROR:%s",e.getMessage()));
				return res;
			}

		}else{
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("service %s not exist",zeppelinInstanceID));
			return res;
		}
	}

	@ApiOperation(value = "获取镜像列表", notes = "get zeppelin images")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/data/zeppelin/images", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getZeppelinImages(HttpServletResponse response, HttpServletRequest request) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		try{
			//1.mysql查询所有镜像
			List<SysZeppelinImage> listImage = imageService.getAllImage();
			res.setCode(ResultInfoDTO.OK);
			res.setData(listImage);
			res.setMessage("ok");
			return res;

		}catch (Exception e){
			e.printStackTrace();
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("获取镜像失败！ERROR:%s",e.getMessage()));
			return res;

		}
	}

	@ApiOperation(value = "获取zeppelin实例状态", notes = "获取zeppelin实例状态")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "userName", value = "用户OA", required = true, dataType = "String")
	@RequestMapping(value = "/data/zeppelin/status", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getTaskStatus(HttpServletResponse response, HttpServletRequest request,String userName) {
		String zeppelinInstanceID = "md4x-zeppelin-"+userName;

		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		try{
			//1.获取任务状态
			String taskStatus = AwsEcsService.getTaskStatus(zeppelinInstanceID);
			res.setCode(ResultInfoDTO.OK);
			res.setData(taskStatus);
			res.setMessage("ok");
			return res;

		}catch (Exception e){
			e.printStackTrace();
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("获取状态失败！ERROR:%s",e.getMessage()));
			return res;

		}
	}

	@ApiOperation(value = "获取zeppelin实例信息", notes = "获取zeppelin实例信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "userName", value = "用户OA", required = true, dataType = "String")
	@RequestMapping(value = "/data/zeppelin/info", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getZeppelinInfo(HttpServletResponse response, HttpServletRequest request, String userName) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		try{
			//1.获取任务状态
			ZeppelinInfoVO zeppelinInfo = zeppelinInstanceService.selectInfoByUserName(userName);
			res.setCode(ResultInfoDTO.OK);
			res.setData(zeppelinInfo);
			res.setMessage("ok");
			return res;

		}catch (Exception e){
			e.printStackTrace();
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("获取zeppelin实例信息失败！ERROR:%s",e.getMessage()));
			return res;

		}
	}

	@ApiOperation(value = "获取zeppelin实例大小列表", notes = "获取zeppelin实例大小列表")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/data/zeppelin/flavors", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getFlavors(HttpServletResponse response, HttpServletRequest request) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		//1.获取实例大小 C1M4，C2M8，C4M24
		List<String> flavors = new ArrayList<>(Arrays.asList("C1M4","C2M8","C4M24"));
		res.setCode(ResultInfoDTO.OK);
		res.setData(flavors);
		res.setMessage("ok");
		return res;
	}

	@ApiOperation(value = "获取用户资源列表", notes = "获取用户资源列表")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/data/zeppelin/resource/info", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getResource(HttpServletResponse response, HttpServletRequest request, String userName) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		List<SysZeppelinResource> resource = zeppelinResourceService.selectResourceByUserName(userName);
		res.setCode(ResultInfoDTO.OK);
		res.setData(resource);
		res.setMessage("ok");
		return res;
	}

	@ApiOperation(value = "收藏资源", notes = "收藏资源")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/data/zeppelin/resource/create", method = RequestMethod.POST)
	public ResultInfoDTO<Object> insertResource(HttpServletResponse response, HttpServletRequest request, @RequestBody SysZeppelinResource zeppelinResource) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		int count = zeppelinResourceService.getCountResourceByUserName(zeppelinResource);
		if(count==0){
			zeppelinResourceService.insertResource(zeppelinResource);
		}
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("collection resource success!");
		return res;
	}

	@ApiOperation(value = "删除资源", notes = "删除资源")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
	@RequestMapping(value = "/data/zeppelin/resource/delete", method = RequestMethod.DELETE)
	public ResultInfoDTO<Object> deleteResource(HttpServletResponse response, HttpServletRequest request, @RequestBody HashMap<String, Integer> paramMap) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		int id = paramMap.get("id");
		int count = zeppelinResourceService.deleteResourceByid(id);
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("delete resource success!");
		return res;
	}

	@ApiOperation(value = "创建项目", notes = "创建项目")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "taskFlavor", value = "实例大小", required = true, dataType = "String"),
			@ApiImplicitParam(name = "imageId", value = "镜像ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "projectName", value = "项目名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "projectDes", value = "项目描述", required = true, dataType = "String"),
			@ApiImplicitParam(name = "datasetId", value = "数据集Id", required = true, dataType = "Json")
	})
	@RequestMapping(value = "/data/zeppelin/project/create", method = RequestMethod.POST)
	@OperationLogDetail(detail = "创建项目",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
	public ResultInfoDTO<Object> createProject(HttpServletResponse response, HttpServletRequest request, @RequestBody ProjectCreateVO projectCreateVO) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		SysZeppelinProject projectEntity = new SysZeppelinProject();
		String userName = getUserNameByToken(request).getData();
		String projectName = projectCreateVO.getProjectName();
		JSONObject datasetId = JSONObject.parseObject(projectCreateVO.getDatasetId());
		projectEntity.setUserName(userName);
		projectEntity.setTaskFlavor(projectCreateVO.getTaskFlavor());
		projectEntity.setImageId(projectCreateVO.getImageId());
		projectEntity.setProjectName(projectName);
		projectEntity.setProjectDes(projectCreateVO.getProjectDes());
		projectEntity.setDatasetId(datasetId);
		projectEntity.setUpdateTime(new Date());
		int count = zeppelinProjectService.getProjectName(userName,projectName);
		if(count==0){
			count = zeppelinProjectService.insertProject(projectEntity);

			int projectId = projectEntity.getId();
			StringBuffer s3Path = new StringBuffer();
			s3Path.append("s3://").append(bucketName).append("/").append("private").append("/").append(userName).append("/")
					.append("result").append("/").append(projectId).append("/");
			projectEntity.setS3Path(s3Path.toString());

			 count = zeppelinProjectService.updateProjectById(projectEntity);

			res.setCode(ResultInfoDTO.OK);
			res.setMessage("create project success!");
		}else{
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("project name %s already exists!", projectName));
		}

		return res;
	}

	@ApiOperation(value = "获取用户所有项目信息", notes = "获取用户所有项目信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/data/zeppelin/project/info", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getProjectInfo(HttpServletResponse response, HttpServletRequest request) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		String userName = getUserNameByToken(request).getData();
		List<SysZeppelinProject> listProject = zeppelinProjectService.getProjectByUserName(userName);
		res.setCode(ResultInfoDTO.OK);
		res.setData(listProject);
		res.setMessage("get project info success!");
		return res;
	}

	@ApiOperation(value = "根据id获取项目信息", notes = "根据id获取项目信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@RequestMapping(value = "/data/zeppelin/project/infoById", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getProjectInfoById(HttpServletResponse response, HttpServletRequest request, int id) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		SysZeppelinProject project = zeppelinProjectService.getProjectById(id);
		res.setCode(ResultInfoDTO.OK);
		res.setData(project);
		res.setMessage("get project info success!");
		return res;
	}

	@ApiOperation(value = "删除项目", notes = "删除项目")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
	@RequestMapping(value = "/data/zeppelin/project/delete", method = RequestMethod.DELETE)
	@OperationLogDetail(detail = "删除项目",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
	public ResultInfoDTO<Object> deleteProjectById(HttpServletResponse response, HttpServletRequest request, @RequestBody HashMap<String, Integer> paramMap) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		int id = paramMap.get("id");
		int count = zeppelinProjectService.deleteProjectById(id);
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("delete project success!");
		return res;
	}

	@ApiOperation(value = "修改项目信息", notes = "修改项目信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "项目id", required = true, dataType = "int"),
			@ApiImplicitParam(name = "taskFlavor", value = "资源大小", required = true, dataType = "String"),
			@ApiImplicitParam(name = "imageId", value = "镜像id", required = true, dataType = "int"),
			@ApiImplicitParam(name = "projectName", value = "项目名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "projectDes", value = "项目描述", required = true, dataType = "String"),
			@ApiImplicitParam(name = "datasetId", value = "数据集Id", required = true, dataType = "Json")
	})
	@RequestMapping(value = "/data/zeppelin/project/update", method = RequestMethod.POST)
	public ResultInfoDTO<Object> updateProjectById(HttpServletResponse response, HttpServletRequest request, @RequestBody SysZeppelinProject sysZeppelinProject) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		int id = sysZeppelinProject.getId();
		String projectName = sysZeppelinProject.getProjectName();
		String userName = getUserNameByToken(request).getData();
		String oldProjectName = zeppelinProjectService.getProjectById(id).getProjectName();
		int count = zeppelinProjectService.getProjectName(userName,projectName);

		if((projectName!=null && projectName.equals(oldProjectName)) || count==0){
			JSONObject datasetId = sysZeppelinProject.getDatasetId()==null?null:JSONObject.parseObject(sysZeppelinProject.getDatasetId().toString());
			sysZeppelinProject.setDatasetId(datasetId);
			sysZeppelinProject.setUpdateTime(new Date());
			count = zeppelinProjectService.updateProjectById(sysZeppelinProject);
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("update project success!");
		}else{
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("project name %s already exists!", projectName));
		}

		return res;
	}

	@ApiOperation(value = "分页查询用户项目列表", notes = "分页查询用户项目列表")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataType = "Integer")})
	@RequestMapping(value = "/data/zeppelin/project/listInfo", method = RequestMethod.GET)
	public ResultInfoDTO<Object> listProjectInfo(HttpServletResponse response, HttpServletRequest request,Integer pageNO, Integer pageSize) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		String userName = getUserNameByToken(request).getData();
		Page<SysZeppelinProject> pager = zeppelinProjectService.listProjects(pageNO, pageSize, userName);
		res.setCode(ResultInfoDTO.OK);
		res.setData(pager);
		res.setMessage("get project by page success!");
		return res;
	}

	@ApiOperation(value = "分页获取用户实例日志列表", notes = "分页获取用户实例日志列表")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataType = "Integer")})
	@RequestMapping(value = "/data/zeppelin/log/listInfo", method = RequestMethod.GET)
	public ResultInfoDTO<Object> listZeppelinLogs(HttpServletResponse response, HttpServletRequest request,Integer pageNO, Integer pageSize) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		String userName = getUserNameByToken(request).getData();
		Page<SysZeppelinLogs> pager = zeppelinLogsService.listLogs(pageNO, pageSize, userName);
		res.setCode(ResultInfoDTO.OK);
		res.setData(pager);
		res.setMessage("get instance logs by page success!");
		return res;
	}

	/**
	 * @Author yaleiwang
	 * @Description 获取项目结果文件
	 * @Date 2020-9-22 16:24
	 * @param response
	 * @param request
	 * @param projectId 项目id
	 * @return cn.com.goldwind.md4x.business.bo.ResultInfoDTO<java.lang.Object>
	 **/
	@ApiOperation(value = "获取项目树状结构结果文件", notes = "获取项目树状结构结果文件")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "projectId", value = "projectId", required = true, dataType = "int")
	@RequestMapping(value = "/data/zeppelin/project/result/treeInfo", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getResultByProjectId(HttpServletResponse response, HttpServletRequest request, Integer projectId) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		SysZeppelinProject project = zeppelinProjectService.getProjectById(projectId);

		if(project!=null){
			String s3Path = project.getS3Path();
			String prefix = s3Path.replace("s3://"+bucketName+"/","");
			List<DownloadFileVO> downloadFileVOTree = s3Service.listDownloadFileInfo(bucketName,prefix);

			res.setCode(ResultInfoDTO.OK);
			res.setData(downloadFileVOTree);
			res.setMessage("get download records success!");

		}else{
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format("projectId %s not exist!",projectId));
		}
		return res;
	}

	/**
	 * @Author yaleiwang
	 * @Description 获取项目结果文件
	 * @Date 2020-9-22 16:24
	 * @param response
	 * @param request
	 * @return cn.com.goldwind.md4x.business.bo.ResultInfoDTO<java.lang.Object>
	 **/
	@ApiOperation(value = "获取项目结果文件V2", notes = "获取项目结果文件V2")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "prefix", value = "prefix", required = true, dataType = "String")
	@RequestMapping(value = "/data/zeppelin/project/result/info", method = RequestMethod.POST)
	public ResultInfoDTO<Object> getResultByPrefix(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> params) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		String prefix = params.get("prefix");

		if (!prefix.contains("/")) {
			int projectId = Integer.parseInt(prefix);
			SysZeppelinProject project = zeppelinProjectService.getProjectById(projectId);
			if (project != null) {
				String s3Path = project.getS3Path();
				prefix = s3Path.replace("s3://" + bucketName + "/", "");
				List<DownloadFileVO> downloadFileVOTree = s3Service.listDownloadFileInfoV2(bucketName, prefix);

				res.setCode(ResultInfoDTO.OK);
				res.setData(downloadFileVOTree);
				res.setMessage("get download records success!");
			} else {
				res.setCode(ResultInfoDTO.FAILED);
				res.setMessage(String.format("projectId %s not exist!", projectId));
			}
		} else {
			List<DownloadFileVO> downloadFileVOTree = s3Service.listDownloadFileInfoV2(bucketName, prefix);
			res.setCode(ResultInfoDTO.OK);
			res.setData(downloadFileVOTree);
			res.setMessage("get download records success!");
		}

		return res;
	}
	
	/**
	 * @Author yaleiwang
	 * @Description 项目结果文件下载
	 * @Date 2020-9-22 17:03
	 * @param response
	 * @param request
	 * @param downloadKeyBO 下载文件json
	 * @return cn.com.goldwind.md4x.business.bo.ResultInfoDTO<java.lang.Object>
	 **/
	@ApiOperation(value = "项目结果文件打包下载", notes = "项目结果文件打包下载")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "projectId", required = true, dataType = "int"),
			@ApiImplicitParam(name = "keys", value = "下载文件列表", required = true, dataType = "Json")
	})
	@RequestMapping(value = "/data/zeppelin/project/result/download", method = RequestMethod.POST)
	public void downloadResult(HttpServletResponse response, HttpServletRequest request, @RequestBody DownloadKeyBO downloadKeyBO) {
		try{
			int projectId = downloadKeyBO.getId();
			SysZeppelinProject project  = zeppelinProjectService.getProjectById(projectId);
			String projectName = project.getProjectName();
			String s3Path = project.getS3Path().replace("s3://"+ bucketName +"/","");
			List<String> keys = downloadKeyBO.getKeys();
			Date downloadDate = new Date();
			// 文件打包下载
			BigDecimal fileSize = s3Service.downloadS3File(bucketName,projectId, projectName, keys, downloadDate,s3Path, response, request);
			// 插入下载记录
			SysResultDownload sysResultDownload = new SysResultDownload();
			sysResultDownload.setUserName(getUserNameByToken(request).getData());
			sysResultDownload.setFileName(keys);
			sysResultDownload.setFileSize(fileSize);
			sysResultDownload.setDownloadTime(downloadDate);
			sysResultDownload.setProjectId(projectId);
			resultDownloadService.insertDownloadRecord(sysResultDownload);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @Author yaleiwang
	 * @Description 结果文件删除
	 * @Date 2020-10-30 22:41
	 * @param response
	 * @param request
	 * @param params
	 * @return cn.com.goldwind.md4x.business.bo.ResultInfoDTO<java.lang.Object>
	 **/
	@ApiOperation(value = "结果文件删除", notes = "结果文件删除")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParam(name = "keys", value = "删除keys", required = true, dataType = "Json")
	@RequestMapping(value = "/data/zeppelin/project/result/delete", method = RequestMethod.DELETE)
	public ResultInfoDTO<Object> deleteResult(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, List<String>> params) {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		try{
			List<String> keys = params.get("keys");
			// 文件删除
			int count = s3Service.deleteS3Files(bucketName,keys);

			System.out.println(keys);
			int deleteCounts = 10;

			res.setCode(ResultInfoDTO.OK);
			res.setMessage("delete success!");

		}catch (Exception ex){
			ex.printStackTrace();
			res.setCode(ResultInfoDTO.FAILED);
			res.setMessage(String.format(ex.getMessage()));
		}

		return res;
	}

}
