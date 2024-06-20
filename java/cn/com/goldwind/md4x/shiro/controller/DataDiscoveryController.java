package cn.com.goldwind.md4x.shiro.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.bo.Md4xWfinfoSummaryVO;
import cn.com.goldwind.md4x.business.bo.WindFarmInfo;
import cn.com.goldwind.md4x.business.bo.WindFarmInfoVO;
import cn.com.goldwind.md4x.business.bo.WindFarmOwnerInfo;
import cn.com.goldwind.md4x.business.bo.WindFarmTypeInfo;
import cn.com.goldwind.md4x.business.entity.datacenter.Md4xWtinfo;
import cn.com.goldwind.md4x.business.service.AwsTransRecordService;
import cn.com.goldwind.md4x.business.service.Md4xWtinfoService;
import cn.com.goldwind.md4x.business.service.Md4xWfinfoService;
import cn.com.goldwind.md4x.config.aop.OperationLogDetail;
import cn.com.goldwind.md4x.config.aop.OperationType;
import cn.com.goldwind.md4x.config.aop.OperationUnit;
import cn.com.goldwind.md4x.mybatis.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "风场风机数据相关接口", tags = { "风场风机信息以及数据完整度" })
@RestController
public class DataDiscoveryController {

	@Autowired
	Md4xWfinfoService md4xWfinfoService;

	@Autowired
	Md4xWtinfoService md4xWtinfoService;

	@Autowired
	AwsTransRecordService awsTransRecordService;
	
	
	// 获取所有风场，分页list
	@ApiOperation(value = "获取所有风场信息", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = false, dataType = "Integer"),
		@ApiImplicitParam(name = "pageNo", value = "页码", required = false, dataType = "Integer"),
		@ApiImplicitParam(name = "province", value = "省份", required = false, dataType = "String"),
		@ApiImplicitParam(name = "searchKey", value = "检索类型,可取值owner、wfname、type", required = false, dataType = "String"),
		@ApiImplicitParam(name = "searchValue", value = "关键字值", required = false, dataType = "String") 
		})
	
	@RequestMapping(value = "/data/windfarms", method = RequestMethod.GET)
	@OperationLogDetail(detail = "查询风场信息",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
	public ResultInfoDTO<Object> getWindFarmInfo(HttpServletResponse response, HttpServletRequest request, Integer pageSize,
			Integer pageNo, String searchKey, String searchValue,String province,String sortFlag) {
	    if(StringUtils.isBlank(sortFlag)) {
	    	sortFlag = "dataIntegrity";//按装机容量排序：wfcapacity ;按数据完整度排序:dataIntegrity
	    }
		Page<WindFarmInfoVO> twfinfo = md4xWfinfoService.getWindFarminfo(pageNo, pageSize, searchKey,searchValue,province,sortFlag.trim());
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("windfarms", twfinfo);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(maps);

		return res;
	}

	// 获取指定风场的所有风机，分页list
	@ApiOperation(value = "获取指定风场的所有风机信息", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = false, dataType = "Integer"),
		@ApiImplicitParam(name = "pageNo", value = "页码", required = false, dataType = "Integer"),
		@ApiImplicitParam(name = "wfName", value = "风场名称", required = false, dataType = "String"),
		})
	
	@RequestMapping(value = "/data/windturbins", method = RequestMethod.GET)
	@OperationLogDetail(detail = "查询风机信息",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
	public ResultInfoDTO<Object> getWindTurbineInfo(HttpServletResponse response, HttpServletRequest request, Integer pageSize,
			Integer pageNo, String wfName) {
		// Page<TWtinfo> twfinfo = tWtinfoService.getTWtinfos(pageNo, pageSize,
		// wfid);//metadata数据源,风机信息
		// Page<TWtinfoVO> twfinfo = tWtinfoService.getTWtinfosFromHuiNeng(pageNo,
		// pageSize, wfid);

		Page<Md4xWtinfo> twfinfo = md4xWtinfoService.getWtinfos(pageNo, pageSize, wfName);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("windturbins", twfinfo);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(maps);
		return res;
	}

	// 数据完整度计算
	@ApiOperation(value = "指定风场风机数据完整度计算", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "dataType", value = "数据类型，目前可取值 7s、B、F、O、W", required = true, dataType = "String"),
		@ApiImplicitParam(name = "startDate", value = "起始日期:yyyy-mm-dd", required = false, dataType = "Date"),
		@ApiImplicitParam(name = "endDate", value = "结束日期:yyyy-mm-dd", required = false, dataType = "Date"),
		@ApiImplicitParam(name = "wfid", value = "风场id,如:140605", required = true, dataType = "String"),
		@ApiImplicitParam(name = "wtids", value = "风机id,多个用半角逗号分隔", required = false, dataType = "String"),
		})
	@OperationLogDetail(detail = "计算数据完整度",level = 1,operationUnit = OperationUnit.TABLE,operationType = OperationType.SELECT)
	@RequestMapping(value = "/data/dataintegrity", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getDataIntegrity(HttpServletResponse response, HttpServletRequest request,
			String dataType, Date startDate, Date endDate, String wfid, String wtids) {
		//if(wtids != null)
		//	wtids = wtids.substring(0, wtids.length()-1);
		Map<String,Object> ret = awsTransRecordService.getDataIntegrity(dataType, startDate, endDate, wfid, wtids);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(ret);

		return res;
	}
	
	// 风场信息汇总  按照省份统计分组，省份名称，总风场数量，总装机容量
	@ApiOperation(value = "按照省份统计风场信息汇总", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	@ApiImplicitParams({
		@ApiImplicitParam(name = "searchKey", value = "检索类型,可取值owner、wfname、type", required = false, dataType = "String"),
		@ApiImplicitParam(name = "searchValue", value = "关键字值", required = false, dataType = "String") 
		})
	
	@RequestMapping(value = "/data/windfarms/summary", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getWindFarmSummary(HttpServletResponse response, HttpServletRequest request,String searchKey, String searchValue) {
		List<Md4xWfinfoSummaryVO> ret = md4xWfinfoService.getMd4xWfinfoSummary(searchKey,searchValue);
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(ret);
		return res;
	}
	
	// 所有风场id和name,用于前端文本框输入时，实时检索
	@ApiOperation(value = "所有风场ID与名称", notes = "用于前端文本框实时检索")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	
	@RequestMapping(value = "/data/windfarms/all", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getWindFarmIdAndName(HttpServletResponse response, HttpServletRequest request) {
		List<WindFarmInfo> ret = md4xWfinfoService.getAllFarmInfos();
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(ret);
		return res;
	}
	
	// 所有机型
	@ApiOperation(value = "风机机型", notes = "用于前端文本框实时检索")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	
	@RequestMapping(value = "/data/windfarms/alltype", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getWindFarmType(HttpServletResponse response, HttpServletRequest request) {
		List<WindFarmTypeInfo> ret = md4xWfinfoService.getAllFarmType();
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(ret);
		return res;
	}
	
	// 所有风场的业主名称,用于前端文本框输入时，实时检索
	@ApiOperation(value = "所有风场业主名称", notes = "用于前端文本框实时检索")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常"), @ApiResponse(code = 400, message = "输入参数不正确"),
			@ApiResponse(code = 403, message = "没有权限访问"), @ApiResponse(code = 500, message = "服务器内部错误") })
	
	@RequestMapping(value = "/data/windfarms/owners", method = RequestMethod.GET)
	public ResultInfoDTO<Object> getWindFarmOwners(HttpServletResponse response, HttpServletRequest request) {
		List<WindFarmOwnerInfo> ret = md4xWfinfoService.getAllOwners();
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData(ret);
		return res;
	}


	// 入参时间格式转换
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
	}
	
	
	/**
	 * 健康检查
	 * @throws Exception 
	 */
	@GetMapping("/data/healthy")
	@ApiOperation(value="健康检查", notes="健康检查")
    public ResultInfoDTO<Object> healthyCheck() throws Exception {
		ResultInfoDTO<Object> res = new ResultInfoDTO<Object>();
		res.setCode(ResultInfoDTO.OK);
		res.setMessage("ok");
		res.setData("good");
		return res;
    }

}
