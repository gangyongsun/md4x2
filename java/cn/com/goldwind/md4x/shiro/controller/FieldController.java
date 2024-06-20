package cn.com.goldwind.md4x.shiro.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.entity.datamartmap.Iecpath;
import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;
import cn.com.goldwind.md4x.business.service.datamart.IIecpathService;
import cn.com.goldwind.md4x.business.service.datamart.IMainFieldService;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.util.CsvUtil;
import cn.com.goldwind.md4x.util.EncodeUtils;
import cn.com.goldwind.md4x.util.StringUtils;
import cn.com.goldwind.md4x.util.i18n.FieldCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @Title: FieldController.java
 * @Package cn.com.goldwind.md4x.shiro.controller
 * @description 字段维护接口，对主变量的CRUD操作
 * @author 孙永刚
 * @date Aug 20, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Api(value = "字段维护接口", tags = { "字段维护接口" })
@RestController
@RequestMapping("/field")
public class FieldController extends BaseController {
	@Autowired
	private IMainFieldService mainFieldService;

	@Autowired
	private IIecpathService iecpathService;

	private final String MAIN_FIELD_ID = "main_field_id";
	private final String MODEL_ENTRY_EN = "model_entry_en";
	private final String IECPATH = "iecpath";

	/**
	 * 根据条件分页查询可分配的源变量列表
	 * 
	 * @param pageNO      页码
	 * @param pageSize    条数
	 * @param findContent 过滤条件
	 * @return
	 */
	@ApiOperation(value = "根据条件分页查询可分配的源变量列表", notes = "根据条件分页查询可分配的源变量列表")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "findContent", value = "查询条件", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "pagelistIecpath", method = RequestMethod.GET)
	@RequiresPermissions("field:pagelistIecpath")
	public ResultInfoDTO<Object> pagelistIecpath(Integer pageNO, Integer pageSize, String findContent) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		if (StringUtils.isNotBlank(findContent)) {
			findContent = findContent.replace(",", "|");
		}
		Page<Iecpath> pager = iecpathService.listIecpath(pageNO, pageSize, findContent);
		if (null != pager) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(FieldCode.IECPATH_LIST_SUCCESS));
			response.setData(pager);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.IECPATH_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 根据条件分页查询主变量列表
	 * 
	 * @param pageNO      页码
	 * @param pageSize    条数
	 * @param findContent 过滤条件
	 * @return
	 */
	@ApiOperation(value = "根据条件分页查询主变量列表", notes = "根据条件分页查询主变量列表")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "findContent", value = "查询条件", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "groupName", value = "分组", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "available", value = "启用状态", required = true, paramType = "query", dataType = "Boolean") })
	@RequestMapping(value = "pageListMainField", method = RequestMethod.GET)
	@RequiresPermissions("field:pageListMainField")
	public ResultInfoDTO<Object> pageListMainField(Integer pageNO, Integer pageSize, String findContent, String groupName, Boolean available,  Boolean needMaintince) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		Page<MainField> pager = mainFieldService.listMainField(pageNO, pageSize, findContent, groupName, available, needMaintince);
		if (null != pager) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_LIST_SUCCESS));
			response.setData(pager);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 更新主变量
	 * 
	 * @param mainField 主变量对象
	 * @return
	 */
	@ApiOperation(value = "更新主变量", notes = "更新主变量")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "mainField", value = "主变量对象", required = true, paramType = "body", dataType = "MainField") })
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	@RequiresPermissions("field:update")
	public ResultInfoDTO<Object> update(@RequestBody MainField mainField) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = mainFieldService.updateById(mainField);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_UPDATE_SUCCESS));
			response.setData(mainField);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_UPDATE_FAILED));
		}
		return response;
	}

	/**
	 * 查询主变量分组信息
	 * 
	 * @param mainField 主变量对象
	 * @return
	 */
	@ApiOperation(value = "查询主变量分组信息", notes = "查询主变量分组信息")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "mainFieldGroups", method = RequestMethod.GET)
	@RequiresPermissions("field:mainFieldGroups")
	public ResultInfoDTO<Object> mainFieldGroups() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<String> groupList = mainFieldService.getGroupList();
		if (null != groupList && groupList.size() > 0) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_GROUPS_LIST_SUCCESS));
			response.setData(groupList);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_GROUPS_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 删除主变量
	 * 
	 * @param id 主变量ID
	 * @return
	 */
	@ApiOperation(value = "删除主变量", notes = "删除主变量")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "主变量", required = true, paramType = "query", dataType = "Integer") })
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	@RequiresPermissions("field:delete")
	public ResultInfoDTO<Object> delete(Integer id) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = mainFieldService.removeById(id);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_DELETE_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_DELETE_FAILED));
		}
		return response;
	}

	/**
	 * 将源变量与主变量做关联：先清空旧关联，再建立新关联
	 * 
	 * @param sourceFieldList 源变量列表
	 * @param customField     主变量
	 * @return
	 */
	@ApiOperation(value = "将源变量与主变量做关联", notes = "将源变量与主变量做关联")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "paramMap", value = "源变量列表和主变量的Map", required = true, paramType = "body", dataType = "object") })
	@RequestMapping(value = "linkIecpathWithMainField", method = RequestMethod.PUT)
	@RequiresPermissions("field:linkIecpathWithMainField")
	public ResultInfoDTO<Object> linkIecpathWithMainField(@RequestBody Map<String, Object> paramMap) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// ***************1.获取传的参数：源变量主键ID列表、主变量主键ID：start***************
		//源变量主键ID列表
		List<Integer> iecpathIdList;
		//主变量主键ID
		Integer mainFieldId;
		try {
			iecpathIdList = (List<Integer>) paramMap.get("iecpathIdList");
			mainFieldId = Integer.parseInt(paramMap.get("mainFieldId").toString());
			if (StringUtils.isBlank(mainFieldId)) {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_ID_BLANK));
				return response;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.API_PARAM_ERROR_ON_MAINFIELD_IECPATH_LINK));
			return response;
		}
		// ***************1.获取传的参数：源变量主键ID列表、主变量主键ID：end***************

		// ***************2.判断主变量是否是Master：start***************
		MainField mainField = mainFieldService.getById(mainFieldId);
		if (!mainField.getSubordinateType().equals(MainField.MASTER)) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_MUST_BE_MASTER));
			return response;
		}
		// ***************2.判断主变量是否是Master：end***************

		// ***************3.清空源变量与当前主变量的关系，并将源变量设置回旧关联关系：start***************
		// 3.1.根据主变量ID查询源变量对象列表
		List<Iecpath> oldIecpathList = iecpathService.list(new QueryWrapper<Iecpath>().eq(MAIN_FIELD_ID, mainFieldId));
		// 3.2.循环遍历源变量列表
		if (null != oldIecpathList && oldIecpathList.size() > 0) {
			// 3.2.1.根据源变量的iecpath寻找原来对应的主变量，如果有则关联其原来的主变量，没有则根据该源变量创建一个新主变量进行关联
			for (Iecpath iecpath : oldIecpathList) {
				// 源变量不能是当前主变量
				if (!iecpath.getIecpath().equals(mainField.getIecpath())) {
					// 3.2.1.1.根据源变量的iecpath寻找原来对应的主变量
					MainField master = mainFieldService.getOne(new QueryWrapper<MainField>().eq(IECPATH, iecpath.getIecpath()));
					// 3.2.1.2.没有则根据该源变量创建一个新主变量
					if (null == master) {
						master = new MainField();
						master.setFlag("2");
						master.setDeid("created_" + iecpath.getId());
						master.setGroupName("新创建");
						master.setGroupSortId(77);
						master.setAvailable(false);
						master.setSubordinateType(MainField.MASTER);
						master.setIecpath(iecpath.getIecpath());
						master.setModelEntryCN(iecpath.getDescrcn());
						master.setModelEntryEN(iecpath.getIecpath());
						boolean createFlag = mainFieldService.save(master);
						if (!createFlag) {
							response.setCode(ResultInfoDTO.FAILED);
							response.setMessage(i18nService.getMessage(FieldCode.MAINFIELD_CREATE_FAILED_ON_THE_BREAKER));
							return response;
						}
					}else {
						//如果存在对应的主变量，则主变量设置回MASTER类型
						master.setSubordinateType(MainField.MASTER);
					}
					// 3.2.1.3.关联主变量
					iecpath.setMainFieldId(master.getId());
					boolean unlinkFlag = iecpathService.updateById(iecpath);
					if (!unlinkFlag) {
						response.setCode(ResultInfoDTO.FAILED);
						StringBuilder message = new StringBuilder();
						message.append(i18nService.getMessage(FieldCode.PART_UNLINK_IECPATH)).append(iecpath.getIecpath())
								.append(i18nService.getMessage(FieldCode.PART_WITH_MAINFIELD)).append(mainFieldId)
								.append(i18nService.getMessage(FieldCode.PART_RELEATIONSHIP_TO_RESTORE));
						response.setMessage(message.toString());
						return response;
					}else {
						//源变量设置回原来的主变量成功后，对其原来的主变量进行更新，主要是将其slave类型更新为master类型
						boolean typeChangFlag= mainFieldService.updateById(master);
						if(!typeChangFlag) {
							response.setCode(ResultInfoDTO.FAILED);
							response.setMessage("源变量对应的主变量从Slave改为Master失败！");
							return response;
						}
					}
				}
			}
		}
		// ***************3.清空源变量与当前主变量的关系，并将源变量设置回旧关联关系：end***************

		// ***************4.建立源变量与主变量的新关系：start***************
		// 4.1.判断源变量ID列表是否为空，为空则通过主变量的en拿到源变量，并且更新源变量的主变量ID，不为空则建立新关联关系
		if (null != iecpathIdList && iecpathIdList.size() > 0) {
			// 4.1.1.根据源变量ID列表查询源变量对象列表
			List<Iecpath> newIecpathList = iecpathService.list(new QueryWrapper<Iecpath>().in("id", iecpathIdList));
			if (null != newIecpathList && newIecpathList.size() > 0) {
				for (Iecpath iecpath : newIecpathList) {
					// 源变量不能是当前主变量
					if (!iecpath.getIecpath().equals(mainField.getIecpath())) {
						// 4.1.1.1.根据源变量的主变量ID查找其主变量，将其更新为奴隶
						MainField master = mainFieldService.getById(iecpath.getMainFieldId());
						master.setSubordinateType(MainField.SLAVE);
						boolean typeChangFlag= mainFieldService.updateById(master);
						if(!typeChangFlag) {
							response.setCode(ResultInfoDTO.FAILED);
							response.setMessage("源变量对应的主变量从Master改为Slave失败！");
							return response;
						}
						// 4.1.1.2.设置与主变量的关联关系
						iecpath.setMainFieldId(mainFieldId);
						boolean linkFlag = iecpathService.updateById(iecpath);
						if (!linkFlag) {
							response.setCode(ResultInfoDTO.FAILED);
							StringBuilder message = new StringBuilder();
							message.append(i18nService.getMessage(FieldCode.PART_LINK_FAILED_AS_IECPATH)).append(iecpath.getIecpath())
									.append(i18nService.getMessage(FieldCode.PART_LINK_FAILED_AS_LINK_MAINFIELD)).append(mainFieldId)
									.append(i18nService.getMessage(FieldCode.PART_LINK_FAILED_AS_LINK_MAINFIELD_FAILED));
							response.setMessage(message.toString());
							return response;
						}
					}
				}
			}
		}
		// ***************4.建立源变量与主变量的新关系：end***************
		mainField.setIecpathList(iecpathService.list(new QueryWrapper<Iecpath>().eq(MAIN_FIELD_ID, mainFieldId)));
		
		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(FieldCode.LINK_IECPAHT_WITH_MAINFIELD_SUCCESS));
		response.setData(mainField);
		return response;
	}

	/**
	 * 上传iecpath文件进行解析入库：t_iecpath，t_main_field
	 * 
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "上传iecpath文件进行解析入库(CSV文件要用UTF-8编码无BOM格式)", notes = "上传iecpath文件进行解析入库(CSV文件要用UTF-8编码无BOM格式)")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile") })
	@RequestMapping(value = "analyzeIecpathCSV", method = { RequestMethod.POST })
//	@RequiresPermissions("field:analyzeIecpathCSV")
	public ResultInfoDTO<Object> analyzeIecpathCSV(@RequestParam(value = "file", required = false) MultipartFile file) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		if (null == file) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.CSV_NONE_EXIST));
			return response;
		}
		if (!file.getOriginalFilename().endsWith(".csv")) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(FieldCode.FILE_IS_NOT_CSV));
			return response;
		}
		try {
			String encode = EncodeUtils.getEncode(file, false);
			if (encode.equals(EncodeUtils.CODE_UTF8_BOM)) {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(FieldCode.CSV_MUST_BE_UTF8_WITHOUT_BOM));
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 解析上传的CSV文件，获取iecpath对象列表
		List<Iecpath> iecpathList = CsvUtil.getCsvData(file, Iecpath.class);

		// 用来记录存入DB成功和失败的记录
		List<Iecpath> successList = new ArrayList<Iecpath>();
		List<Iecpath> failedList = new ArrayList<Iecpath>();

		// 根据iecpath源变量列表创建主变量和源变量
		for (Iecpath iecpath : iecpathList) {
			String iecpathProp = iecpath.getIecpath();
			// 1.根据iecpath属性查询是否存在相同iecpath的源变量
			Iecpath iecpathChecker = iecpathService.getOne(new QueryWrapper<Iecpath>().eq(IECPATH, iecpathProp));
			if (null != iecpathChecker) {
				failedList.add(iecpath);
				break;
			}
			// 2.根据iecpathProp属性查询是否存在对应的主变量
			MainField master = mainFieldService.getOne(new QueryWrapper<MainField>().eq(MODEL_ENTRY_EN, iecpathProp));
			// 2.1.不存在则根据该源变量创建一个新主变量，进行关联
			if (null == master) {
				master = new MainField();
				master.setFlag("2");
				master.setDeid("imported_" + System.currentTimeMillis());
				master.setGroupName("新导入");
				master.setGroupSortId(78);
				master.setAvailable(false);
				master.setSubordinateType(MainField.MASTER);
				master.setModelEntryCN(iecpath.getDescrcn());
				master.setModelEntryEN(iecpath.getIecpath());
				mainFieldService.save(master);
			}
			// 关联主变量
			iecpath.setMainFieldId(master.getId());
			// 创建源变量
			boolean createIecpathFlag = iecpathService.save(iecpath);
			if (createIecpathFlag) {
				successList.add(iecpath);
			}
		}

		StringBuilder message = new StringBuilder();
		message.append(i18nService.getMessage(FieldCode.FILE_PARSE_SUCCESS_AS))
				.append(successList.size()).append(i18nService.getMessage(FieldCode.FILE_PARSE_SUCCESS_AS_SAVE_INTO_DB_SUCCESS)).append(",")
				.append(failedList.size()).append(i18nService.getMessage(FieldCode.FILE_PARSE_SUCCESS_AS_SAVE_INTO_DB_FAILED)).append("!");

		Map<String, List<Iecpath>> resultMap = new HashMap<String, List<Iecpath>>();
		resultMap.put(i18nService.getMessage(FieldCode.FILE_PARSE_SUCCESS_AS_SAVE_INTO_DB_SUCCESS), successList);
		resultMap.put(i18nService.getMessage(FieldCode.FILE_PARSE_SUCCESS_AS_SAVE_INTO_DB_FAILED), failedList);

		response.setCode(ResultInfoDTO.OK);
		response.setMessage(message.toString());
		response.setData(resultMap);
		return response;
	}

}
