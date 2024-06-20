package cn.com.goldwind.md4x.shiro.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.goldwind.md4x.config.aop.OperationLogDetail;
import cn.com.goldwind.md4x.config.aop.OperationType;
import cn.com.goldwind.md4x.config.aop.OperationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.service.AwsAthenaService;
import cn.com.goldwind.md4x.business.service.AwsGlueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "数据集抽取-工作流相关接口", tags = { "数据集抽取-工作流相关接口" })
@RestController
public class WorkflowController {
	
	@Autowired
	AwsGlueService awsGlueService;
	
	@Autowired
	AwsAthenaService awsAthenaService;
	
	
	// 启动AWS工作流，进行数据抽取
		@ApiOperation(value = "启动aws glue workflow", notes = "进行数据抽取")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
				@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
		
		@ApiImplicitParams({
			@ApiImplicitParam(name = "datasetId", value = "数据集id", required = true, dataType = "String") ,
			@ApiImplicitParam(name = "datasetScope", value = "数据据集可视范围", required = true, dataType = "String") ,
			@ApiImplicitParam(name = "datasetOwner", value = "数据集属主", required = true, dataType = "String") 
			})
		
		@RequestMapping(value = "/data/startworkflow", method = RequestMethod.GET)
		@OperationLogDetail(detail = "开始抽取数据集",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
		public ResultInfoDTO<Object> startWorkflow(HttpServletResponse response, HttpServletRequest request,String datasetId,String datasetScope,String datasetOwner) {
			String ret = awsGlueService.startWorkflow(datasetId,datasetScope,datasetOwner);
			ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("ok");
			res.setData(ret);
			return res;
		}
		
		// 停止AWS工作流
		@ApiOperation(value = "停止aws glue workflow", notes = "停止数据抽取")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
				@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
		
		@ApiImplicitParams({
			@ApiImplicitParam(name = "datasetId", value = "数据集id", required = true, dataType = "String") ,
			@ApiImplicitParam(name = "datasetScope", value = "数据据集可视范围", required = true, dataType = "String") ,
			@ApiImplicitParam(name = "datasetOwner", value = "数据集属主", required = true, dataType = "String") 
			})
		
		@RequestMapping(value = "/data/stopworkflow", method = RequestMethod.GET)
		@OperationLogDetail(detail = "停止抽取数据集",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
		public ResultInfoDTO<Object> stopWorkflow(HttpServletResponse response, HttpServletRequest request,String datasetId) {
			String ret = awsGlueService.stopWorkflow(datasetId);
			ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("ok");
			res.setData(ret);
			return res;
		}
		
		
		// 启动AWS作业，进行数据抽取
		@ApiOperation(value = "启动作业", notes = "启动aws glue作业")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
				@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
		
		@ApiImplicitParams({
			@ApiImplicitParam(name = "jobName", value = "作业名称", required = true, dataType = "String") 
			})
		
		@RequestMapping(value = "/data/startjob", method = RequestMethod.GET)
		public ResultInfoDTO<Object> startJob(HttpServletResponse response, HttpServletRequest request,String jobName) {
			String ret = awsGlueService.startJob(jobName);
			ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("ok");
			res.setData(ret);
			return res;
		}
		

		// 查询工作流状态
		@ApiOperation(value = "查询工作流状态", notes = "查询工作流状态")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
				@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
		
		@ApiImplicitParams({
			@ApiImplicitParam(name = "workflowName", value = "工作流名称", required = true, dataType = "String"),
			@ApiImplicitParam(name = "runId", value = "工作流运行id", required = true, dataType = "String") 
			})
		
		@RequestMapping(value = "/data/workflowstatus", method = RequestMethod.GET)
		public ResultInfoDTO<Object> getWorkflowStatus(HttpServletResponse response, HttpServletRequest request,String datasetId) {
			String ret = awsGlueService.getWorkflowsStatus(datasetId);
			ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("ok");
			res.setData(ret);
			return res;
		}
		
		//  删除数据集id对应的工作流、触发器、crawler
		@ApiOperation(value = "删除工作流", notes = "删除工作流")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
				@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
		
		@ApiImplicitParams({
			@ApiImplicitParam(name = "datasetId", value = "数据集id yyyyMMddHHmmssSSS", required = true, dataType = "String"),
			})
		
		@RequestMapping(value = "/data/workflow", method = RequestMethod.DELETE)
		@OperationLogDetail(detail = "删除数据集",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
		public ResultInfoDTO<Object> deleteWorkflow(HttpServletResponse response, HttpServletRequest request,String datasetId) {
			String ret = awsGlueService.deleteWorkflow(datasetId);
			ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
			res.setCode(ResultInfoDTO.OK);
			res.setMessage("ok");
			res.setData(ret);
			return res;
		}
		
		
		
		@ApiOperation(value = "athena数据集预览", notes = "数据集预览")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
				@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
		@ApiImplicitParams({
			@ApiImplicitParam(name = "datasetId", value = "数据集id", required = true, dataType = "String") ,
			@ApiImplicitParam(name = "datasetScope", value = "数据据集可视范围", required = true, dataType = "String") ,
			@ApiImplicitParam(name = "datasetOwner", value = "数据集属主", required = true, dataType = "String") 
			})
		@RequestMapping(value = "/data/athena", method = RequestMethod.GET)
		public ResultInfoDTO<Object> athenaQuery(HttpServletResponse response, HttpServletRequest request,String datasetId,String datasetScope,String datasetOwner) {
			List<Map<String, String>> ret = null;
			ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
			try {
				ret = awsAthenaService.getQueryResult(datasetId, datasetScope, datasetOwner);
			} catch (Exception e) {
				res.setMessage(e.getMessage());
			}
			res.setCode(ResultInfoDTO.OK);
			res.setData(ret);  
			return res;
		}
}
