package cn.com.goldwind.md4x.shiro.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.entity.datamartmap.Collection;
import cn.com.goldwind.md4x.business.entity.datamartmap.CollectionMainfield;
import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;
import cn.com.goldwind.md4x.business.service.datamart.ICollectionMainfieldService;
import cn.com.goldwind.md4x.business.service.datamart.ICollectionService;
import cn.com.goldwind.md4x.business.service.datamart.IMainFieldService;
import cn.com.goldwind.md4x.shiro.domain.entity.User;
import cn.com.goldwind.md4x.shiro.service.IUserService;
import cn.com.goldwind.md4x.util.StringUtils;
import cn.com.goldwind.md4x.util.i18n.CollectionCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @Title: CollectionController.java
 * @Package cn.com.goldwind.md4x.shiro.controller
 * @description 用户变量收藏夹前端控制器
 * @author 孙永刚
 * @date Sep 9, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Api(value = "用户常用变量收藏夹接口", tags = { "用户常用变量收藏夹接口" })
@RestController
@RequestMapping("/collection")
public class CollectionController extends BaseController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IMainFieldService mainFieldService;

	@Autowired
	private ICollectionService collectionService;

	@Autowired
	private ICollectionMainfieldService collectionMainfieldService;

	/**
	 * 收藏变量，创建变量收藏夹
	 * 
	 * @param datasetScope       private或public
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "收藏变量，创建变量收藏夹", notes = "收藏变量，创建变量收藏夹")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "createCollection", method = { RequestMethod.POST })
	@ApiImplicitParams({ @ApiImplicitParam(name = "paramMap", value = "参数Map", required = true, dataType = "Map") })
	@RequiresPermissions("collection:createCollection")
	public ResultInfoDTO<Object> createCollection(@RequestBody Map<String, Object> paramMap, HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();

		// ***************1.获取传的参数：收藏夹名、主变量ID列表：start***************
		List<Integer> mainFieldIdList;
		String collectionName;
		try {
			collectionName = paramMap.get("collectionName").toString();
			mainFieldIdList = (List<Integer>) paramMap.get("mainFieldIdList");
			if (StringUtils.isBlank(collectionName)) {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(CollectionCode.COLLECTION_NAME_EMTPY));
				return response;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(CollectionCode.API_PARAMATER_ERROR));
			return response;
		}
		// ***************1.获取传的参数：收藏夹名、主变量ID列表：end***************

		// 2.根据token获取用户名(OA号)，根据OA号获取当前用户
		String userName = getUserNameByToken(httpServletRequest).getData();
		User user = userService.getOne(new QueryWrapper<User>().eq("user_name", userName));

		// ***************3.根据用户ID和收藏夹名创建用户变量收藏夹：start***************
		Collection collection = collectionService.getOne(new QueryWrapper<Collection>().eq("collection_name", collectionName).eq("user_id", user.getUserId()));
		if (null != collection) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(CollectionCode.EXISTED_COLLECTION));
			return response;
		}
		collection = new Collection();
		collection.setUserId(user.getUserId());
		collection.setCollectionName(collectionName);
		boolean flag = collectionService.save(collection);
		if (!flag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(CollectionCode.COLLECTION_CREATE_FAILED));
			return response;
		}
		// ***************3.根据用户ID和收藏夹名创建用户变量收藏夹：end***************

		// ***************4.根据收藏夹ID和主变量ID列表创建收藏夹和主变量的关系：start*************
		for (Integer mainFieldId : mainFieldIdList) {
			CollectionMainfield collectionMainfield = new CollectionMainfield();
			collectionMainfield.setCollectionId(collection.getId());
			collectionMainfield.setMainfieldId(mainFieldId);
			collectionMainfieldService.save(collectionMainfield);
		}
		// ***************4.根据收藏夹ID和主变量ID列表创建收藏夹和主变量的关系：end***************

		// ***************4.根据收藏夹ID查询主变量ID列表，根据主变量ID列表查询主变量：start***************
		if (null != collection) {
			List<Integer> releatedMainFieldIdList = new ArrayList<Integer>();
			List<CollectionMainfield> collectionMainfieldList = collectionMainfieldService
					.list(new QueryWrapper<CollectionMainfield>().eq("collection_id", collection.getId()));
			for (CollectionMainfield collectionMainfield : collectionMainfieldList) {
				releatedMainFieldIdList.add(collectionMainfield.getMainfieldId());
			}
			// 得到相关主变量
			List<MainField> mainFieldList = (List<MainField>) mainFieldService.listByIds(releatedMainFieldIdList);
			// 设置相关主变量
			collection.setMainFieldList(mainFieldList);
		}
		// ***************4.根据收藏夹ID查询主变量ID列表，根据主变量ID列表查询主变量：end***************

		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(CollectionCode.FIELD_COLLECT_SUCCESS));
		response.setData(collection);
		return response;
	}

	/**
	 * 查询变量收藏夹列表
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "查询变量收藏夹列表", notes = "查询变量收藏夹列表")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "list", method = { RequestMethod.GET })
	@RequiresPermissions("collection:list")
	public ResultInfoDTO<Object> list(HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<Collection> collectionList = getCollectionList(httpServletRequest);
		if (null != collectionList && collectionList.size() > 0) {
			for (Collection collection : collectionList) {
				// ***************4.根据收藏夹ID查询主变量ID列表，根据主变量ID列表查询主变量：start***************
				List<Integer> releatedMainFieldIdList = new ArrayList<Integer>();
				List<CollectionMainfield> collectionMainfieldList = collectionMainfieldService
						.list(new QueryWrapper<CollectionMainfield>().eq("collection_id", collection.getId()));
				for (CollectionMainfield collectionMainfield : collectionMainfieldList) {
					releatedMainFieldIdList.add(collectionMainfield.getMainfieldId());
				}
				// 得到相关主变量
				List<MainField> mainFieldList = (List<MainField>) mainFieldService.listByIds(releatedMainFieldIdList);
				// 设置相关主变量
				collection.setMainFieldList(mainFieldList);
				// ***************4.根据收藏夹ID查询主变量ID列表，根据主变量ID列表查询主变量：end***************
			}
		} else {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(CollectionCode.NON_COLLECTION));
			response.setData(collectionList);
			return response;
		}

		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(CollectionCode.COLLECTION_LOAD_SUCCESS));
		response.setData(collectionList);
		return response;
	}

	/**
	 * 删除变量收藏夹
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "删除变量收藏夹", notes = "删除变量收藏夹")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "collectionId", value = "收藏夹ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "remove", method = { RequestMethod.DELETE })
	@RequiresPermissions("collection:remove")
	public ResultInfoDTO<Object> remove(Integer collectionId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1.先删除收藏夹与主变量的关系
		boolean flag = collectionMainfieldService.remove(new QueryWrapper<CollectionMainfield>().eq("collection_id", collectionId));
		if (!flag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(CollectionCode.COLLECTION_FIELD_BREAKOUT_FAILED));
			return response;
		}

		// 2.删除收藏夹
		flag = collectionService.removeById(collectionId);
		if (!flag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(CollectionCode.COLLECTION_DELETE_FAILED));
			return response;
		}

		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(CollectionCode.COLLECTION_DELETE_SUCCESS));
		return response;
	}

	/**
	 * 获取用户变量收藏夹列表
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	private List<Collection> getCollectionList(HttpServletRequest httpServletRequest) {
		// 根据token获取用户名(OA号)
		String userName = getUserNameByToken(httpServletRequest).getData();
		// 根据OA号获取当前用户
		User user = userService.getOne(new QueryWrapper<User>().eq("user_name", userName));
		// 根据用户ID获取收藏夹列表
		List<Collection> collectionList = collectionService.list(new QueryWrapper<Collection>().eq("user_id", user.getUserId()));
		return collectionList;
	}

}
