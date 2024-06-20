package cn.com.goldwind.md4x.shiro.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.bo.datamart.DatamartExtractBO;
import cn.com.goldwind.md4x.business.bo.datamart.DatamartResultSaveBO;
import cn.com.goldwind.md4x.business.bo.datamart.MainFieldGroupBO;
import cn.com.goldwind.md4x.business.entity.datamart.ExparamDatamart;
import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;
import cn.com.goldwind.md4x.business.service.datamart.IExparamDatamartService;
import cn.com.goldwind.md4x.business.service.datamart.IMainFieldService;
import cn.com.goldwind.md4x.config.aop.OperationLogDetail;
import cn.com.goldwind.md4x.config.aop.OperationType;
import cn.com.goldwind.md4x.config.aop.OperationUnit;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.util.DatamartUtil;
import cn.com.goldwind.md4x.util.DateUtil;
import cn.com.goldwind.md4x.util.i18n.DatamartCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @Title: DatamartContoller.java
 * @Package cn.com.goldwind.md4x.shiro.controller
 * @description 数据集控制类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 4, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Api(value = "数据集接口", tags = { "数据集接口" })
@RestController
@RequestMapping("/datamart")
public class DatamartContoller extends BaseController {
	@Autowired
	private IExparamDatamartService exparamDatamartService;

	@Autowired
	private IMainFieldService mainFieldService;

	@Value("${aws.s3bucketName}")
	private String bucketName;

	@Value("${aws.s3bucketPrefix}")
	private String s3bucketPrefix;

	@Value("${aws.awsGlueDataCatalogPrefix}")
	private String awsGlueDataCatalogPrefix;

	/**
	 * 获取抽取数据映射字段
	 * 
	 * @param datamartExtract
	 * @return
	 */
	@ApiOperation(value = "获取抽取数据映射字段", notes = "获取抽取数据映射字段")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "datamartExtract", value = "数据集抽取业务对象", required = true, dataType = "DatamartExtractBO") })
	@RequestMapping(value = "listVariables", method = RequestMethod.POST)
	@RequiresPermissions("datamart:listVariables")
	@OperationLogDetail(detail = "获取映射字段", level = 1, operationUnit = OperationUnit.TABLE, operationType = OperationType.SELECT)
	public ResultInfoDTO<Object> listVariables(@RequestBody DatamartExtractBO datamartExtract) {
		ResultInfoDTO<Object> resultInfo = new ResultInfoDTO<Object>();
		// 1.参数验证
		if (null == datamartExtract) {
			resultInfo.setCode(ResultInfoDTO.FAILED);
			resultInfo.setMessage(i18nService.getMessage(DatamartCode.API_PARAMATER_ERROR_DATAMARTEXTRACT_NULL));
			return resultInfo;
		}

		// 2.参数附加
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wfid", datamartExtract.getWfid());
		map.put("wtids", datamartExtract.getWtids());
		map.put("file_type", datamartExtract.getFile_type());
		map.put("begin_time", datamartExtract.getBegin_time());
		map.put("end_time", datamartExtract.getEnd_time());

		// 3.查询字段分组列表
		List<String> groupList = mainFieldService.getGroupList();

		// 4.分组查询字段映射信息
		List<MainFieldGroupBO> mainFieldGroupBOList = generateFieldMapTreeData(datamartExtract.getProtocolIds(), groupList);

		if (mainFieldGroupBOList.size() > 0) {
			map.put("labels", mainFieldGroupBOList);
			resultInfo.setData(map);
			resultInfo.setCode(ResultInfoDTO.OK);
			resultInfo.setMessage(i18nService.getMessage(DatamartCode.LOAD_FIELD_SUCCESS));
		} else {
			resultInfo.setCode(ResultInfoDTO.FAILED);
			resultInfo.setMessage(i18nService.getMessage(DatamartCode.LOAD_FIELD_FAILED));
		}
		return resultInfo;
	}

	/**
	 * 生成前端字段树数据
	 * 
	 * @param protocolIds 协议ID列表
	 * @param groupList   字段分组列表
	 * @return 字段树数据
	 */
	private List<MainFieldGroupBO> generateFieldMapTreeData(List<Integer> protocolIds, List<String> groupList) {
		List<MainFieldGroupBO> mainFieldGroupList = new ArrayList<MainFieldGroupBO>();
		for (int i = 0; i < groupList.size(); i++) {
			String groupName = groupList.get(i);
			// 1.根据分组信息和协议列表查询主变量列表
			List<MainField> mainFieldList = mainFieldService.getMainFieldList(groupName, protocolIds);
			if (mainFieldList != null && mainFieldList.size() > 0) {
				// 2.父亲设置孩子列表
				MainFieldGroupBO mainFieldGroup = new MainFieldGroupBO();
				mainFieldGroup.setMainFieldList(mainFieldList);
				mainFieldGroup.setGroupId(i + 1);
				mainFieldGroup.setLabel(groupName);
				// 3.父亲列表加父亲
				mainFieldGroupList.add(mainFieldGroup);
			}
		}
		return mainFieldGroupList;
	}

	/**
	 * 保存抽取数据条件，创建数据集
	 * 
	 * @param datamartResult 数据集保存结果业务对象
	 * @return
	 */
	@ApiOperation(value = "保存抽取数据条件，创建数据集", notes = "保存抽取数据条件，创建数据集")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "datamartResult", value = "数据集保存结果业务对象", required = true, dataType = "DatamartResultSaveBO") })
	@RequestMapping(value = "saveDataSet", method = RequestMethod.POST)
	@RequiresPermissions("datamart:saveDataSet")
	public ResultInfoDTO<Object> saveDataSet(@RequestBody DatamartResultSaveBO datamartResult) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		ExparamDatamart exparamDatamart = new ExparamDatamart();
		exparamDatamart.setFileType(datamartResult.getFileType());
		exparamDatamart.setBeginTime(DateUtil.stringDate2Date(datamartResult.getBeginTime(), "yyyy-MM-dd"));
		exparamDatamart.setEndTime(DateUtil.stringDate2Date(datamartResult.getEndTime(), "yyyy-MM-dd"));
		exparamDatamart.setDatamartName(datamartResult.getDatamartName());
		exparamDatamart.setRemarks(datamartResult.getRemarks());

		// 1.从条件中获取变量列表
		List<MainField> conditions = datamartResult.getConditions();

		if (null != conditions && conditions.size() > 0) {
			List<Integer> mainFieldIds = new ArrayList<Integer>();
			// 2.从变量列表参数中获取ID
			for (MainField mainField : conditions) {
				mainFieldIds.add(mainField.getId());
			}
			// 3.根据ID列表从数据库查询得到主变量列表
			List<MainField> listFromDB = (List<MainField>) mainFieldService.listByIds(mainFieldIds);
			// 4.设置主变量权重，更新主变量
			for (MainField mainField : listFromDB) {
				mainField.setWeights(mainField.getWeights() + 1);
				mainFieldService.updateById(mainField);
			}
		}

		// CustomModelEntryEN的值由ModelEntryEN的值进行生成，“.”用“_”代替;
		// condition和conditionValue为null的情况下，进行处理事condition="",conditionValue=0
		List<MainField> list = DatamartUtil.dealWithList(conditions);

		// 注释掉德阳的代码
		// exparamDatamart.setParamContent(list);
		// 将List数组转成json字符串
		String LabelChildSave_json = JSON.toJSONString(list);
		exparamDatamart.setParamContent(LabelChildSave_json);
		exparamDatamart.setWfcount(1);
		exparamDatamart.setDmStatus(datamartResult.getDmStatus() == null ? 0 : datamartResult.getDmStatus());
		exparamDatamart.setWfid(datamartResult.getWfid());

		// 注释掉德阳的代码
		// exparamDatamart.setWtid(datamartResult.getWtids());
		// 将List数组转成json字符串
		String wtid_json = JSON.toJSONString(datamartResult.getWtids());
		exparamDatamart.setWtid(wtid_json);

		exparamDatamart.setDatasetId(DateUtil.date2StringPattern(new Date(), "yyyyMMddHHmmssSSS"));
		exparamDatamart.setDatasetOwner(datamartResult.getUsername());
		exparamDatamart.setDatasetScope("private");
		exparamDatamart.setCreateTime(new Date());
		exparamDatamart.setUserName(datamartResult.getUsername());
		exparamDatamart.setIsDel(0);
		exparamDatamart.setBucketName(bucketName);
		exparamDatamart.setS3Path(DatamartUtil.createS3Path(exparamDatamart.getBucketName(), s3bucketPrefix));
		exparamDatamart.setAthenaDb(DatamartUtil.createAthenaDBorTable(awsGlueDataCatalogPrefix, exparamDatamart.getDatasetScope(),
				exparamDatamart.getDatasetOwner(), exparamDatamart.getDatasetId(), true));
		exparamDatamart.setAthenaTable(DatamartUtil.createAthenaDBorTable(awsGlueDataCatalogPrefix, exparamDatamart.getDatasetScope(),
				exparamDatamart.getDatasetOwner(), exparamDatamart.getDatasetId(), false));
		exparamDatamart.setWfName(datamartResult.getWfname());
		exparamDatamart.setDataIntegrity(datamartResult.getDataIntegrity());
		exparamDatamart.setVariableNum(list == null ? 0 : list.size());
		boolean flag = exparamDatamartService.save(exparamDatamart);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_CREATED_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_CREATED_FAILED));
		}
		return response;
	}

	/**
	 * 根据用户名(或OA号)分页查询数据集列表
	 * 
	 * @param currPage     页码
	 * @param pageSize     每页条数
	 * @param datasetScope "private或public"
	 * @return
	 */
	@ApiOperation(value = "根据用户名查询数据集列表", notes = "根据用户名查询数据集列表")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "currPage", value = "页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "datasetScope", value = "范围", required = true, dataType = "String"),
			@ApiImplicitParam(name = "available", value = "是否是抽取完成并且可用的数据集", required = true, dataType = "Boolean") })
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@RequiresPermissions("datamart:list")
	public ResultInfoDTO<Object> list(int currPage, int pageSize, String datasetScope, Boolean available, HttpServletRequest httpServletRequest) {
		Map<String, Object> conditions = new HashMap<String, Object>();
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1.available判空
		if (null != available) {
			conditions.put("available", available);
		}
		// 2.scope判空
		if (null != datasetScope) {
			conditions.put("datasetScope", datasetScope.trim());
			// 2.1.scope为private时要设置用户名
			if (datasetScope.trim().equals(DATA_SCOPE_PRIVATE)) {
				String userName = getUserNameByToken(httpServletRequest).getData();
				if (StringUtils.isNotBlank(userName)) {
					conditions.put("userName", userName);
				}
			}
		}

		// 3.业务逻辑
		Page<ExparamDatamart> list = exparamDatamartService.pageListDatamart(currPage, pageSize, conditions);

		if (null != list) {
			response.setData(list);
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_LIST_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 数据集重命名
	 * 
	 * @param id                 数据集ID
	 * @param datamartName       数据集新名称
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "数据集重命名", notes = "数据集重命名")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "数据集ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "datamartName", value = "数据集新名称", required = true, dataType = "String") })
	@RequestMapping(value = "rename", method = RequestMethod.PUT)
	@RequiresPermissions("datamart:rename")
	public ResultInfoDTO<Object> rename(Integer id, String datamartName,String remarks, HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		if (null == id) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_ID_NULL_ON_RENAME));
			return response;
		}
		if (null == datamartName || "".equals(datamartName.trim())) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_NEWNAME_EMPTY));
			return response;
		}

		// 1.先根据ID查询存在性
		ExparamDatamart exparamDatamart = exparamDatamartService.getById(id);
		if (null == exparamDatamart) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_NONE_EXIST_ON_RENAME));
			return response;
		}
		// 2.设置数据集新名称和更新时间
		exparamDatamart.setDatamartName(datamartName);
		exparamDatamart.setRemarks(remarks);
		exparamDatamart.setUpdateTime(new Date());

		// 3.更新
		boolean flag = exparamDatamartService.updateById(exparamDatamart);

		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_RENAME_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_RENAME_FAILED));
		}
		return response;
	}

	/**
	 * 删除数据集(逻辑删除，通过修改isDel字段为1来操作)
	 * 
	 * @param id 数据集ID
	 * @return
	 */
	@ApiOperation(value = "删除数据集", notes = "删除数据集")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "数据集ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "delete", method = RequestMethod.PUT)
	@RequiresPermissions("datamart:delete")
	public ResultInfoDTO<Object> delete(Integer id) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		if (null == id || id.toString().equals("")) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_ID_NULL_ON_DELETE));
			return response;
		}

		// 1.查询存在性
		ExparamDatamart exparamDatamart = exparamDatamartService.getById(id);
		if (null == exparamDatamart) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_NONE_EXIST_ON_DELETE));
			return response;
		}
		// 2.设置删除字段
		exparamDatamart.setIsDel(1);

		// 3.更新
		boolean flag = exparamDatamartService.updateById(exparamDatamart);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_DELETE_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_DELETE_FAILED));
		}
		return response;
	}

	/**
	 * 更新数据集状态
	 * 
	 * @param id       数据集ID
	 * @param dmStatus 状态代码
	 * @return
	 */
	@ApiOperation(value = "更新数据集状态", notes = "更新数据集状态")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "数据集ID", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "dmStatus", value = "数据集状态值", required = true, dataType = "Integer") })
	@RequestMapping(value = "updateStatus", method = RequestMethod.PUT)
	@RequiresPermissions("datamart:updateStatus")
	public ResultInfoDTO<Object> updateStatus(Integer id, Integer dmStatus) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		if (null == id || null == dmStatus) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_ID_NULL_ON_STATUS_UPDATE));
			return response;
		}

		// 1.查询存在性
		ExparamDatamart exparamDatamart = exparamDatamartService.getById(id);
		if (exparamDatamart == null) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_NONE_EXIST_ON_STATUS_UPDATE));
			return response;
		}
		// 2.设置数据集状态码、更新时间
		exparamDatamart.setDmStatus(dmStatus);
		exparamDatamart.setUpdateTime(new Date());
		if (dmStatus == 2) {
			exparamDatamart.setExtStatus(100);
		}
		// 3.更新
		boolean flag = exparamDatamartService.updateById(exparamDatamart);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_STATUS_UPDATE_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(DatamartCode.DATASET_STATUS_UPDATE_FAILED));
		}
		return response;
	}

}
