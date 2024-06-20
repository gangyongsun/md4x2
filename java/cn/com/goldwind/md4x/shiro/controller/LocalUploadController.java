package cn.com.goldwind.md4x.shiro.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.bo.upload.uploadFileBO;
import cn.com.goldwind.md4x.business.entity.datamart.S3UploadfileIndex;
import cn.com.goldwind.md4x.business.service.AwsS3Service;
import cn.com.goldwind.md4x.business.service.datamart.S3UploadfileIndexService;
import cn.com.goldwind.md4x.util.DatamartUtil;
import cn.com.goldwind.md4x.util.i18n.LocalUploadCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @Title: LocalUploadController.java
 * @Package cn.com.goldwind.md4x.shiro.controller
 * @description 本地上传控制类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 4, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Api(value = "文件上传接口", tags = { "文件上传接口" })
@RestController
@RequestMapping("/localupload")
public class LocalUploadController extends BaseController {
	@Autowired
	private AwsS3Service s3Service;

	@Autowired
	private S3UploadfileIndexService s3UploadfileIndexService;
	/**
	 * AWS S3桶名
	 */
	@Value("${aws.s3bucketName}")
	private String bucketName;

	/**
	 * 待处理的源数据s3桶前缀
	 */
	@Value("${aws.s3bucketPrefix}")
	private String s3bucketPrefix;

	/**
	 * 上传文件到AWS S3桶
	 * 
	 * @param file
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "上传文件到s3桶", notes = "上传文件到s3桶")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile") })
	@RequestMapping(value = "uploadFile", method = { RequestMethod.POST })
	@RequiresPermissions("localupload:uploadFile")
	public ResultInfoDTO<Object> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1.根据token获取用户名(OA号)
		String userName = getUserNameByToken(httpServletRequest).getData();
		if (null == file) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_UPLOAD_FAILED_ON_FILE_NONE_EXIST));
			return response;
		}
		// 构建S3文件key字符串
		String pathKey = DatamartUtil.createS3PathKey(bucketName, DATA_SCOPE_PRIVATE, userName);
		// 上传到S3
		boolean flag = s3Service.uploadFileToS3(file, pathKey, bucketName);
		if (flag) {
			// 获取上传文件的全名
			String fileName = file.getOriginalFilename();
			// 组件S3文件key
			StringBuilder s3Key = new StringBuilder();
			s3Key.append("s3://").append(pathKey).append("/").append(fileName);

			// 存在性判断
			S3UploadfileIndex entity = s3UploadfileIndexService.getOne(new QueryWrapper<S3UploadfileIndex>().eq("s3_key", s3Key.toString()));

			boolean saveFlag = false;
			if (null == entity) {
				entity = new S3UploadfileIndex();
				entity.setS3Key(s3Key.toString());
				// 保存s3key信息到数据库,如果有就更新
				saveFlag = s3UploadfileIndexService.saveOne(entity);
			} else {
				entity.setS3Key(s3Key.toString());
				saveFlag = s3UploadfileIndexService.updateOne(entity);
			}

			if (saveFlag) {
				response.setCode(ResultInfoDTO.OK);
				response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_UPLOAD_SUCCESS));
			} else {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_UPLOAD_SUCCESS_WHILE_S3KEY_SAVE_FAILED));
			}
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_UPLOAD_FAILED));
		}
		return response;
	}

	/**
	 * 文件列表
	 * 
	 * @param datasetScope       private或public
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "本地上传文件列表", notes = "本地上传文件列表")
	@RequestMapping(value = "list", method = { RequestMethod.GET })
	@RequiresPermissions("localupload:list")
	public ResultInfoDTO<Object> list(@RequestParam(value = "datasetScope", required = false) String datasetScope, HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1.根据token获取用户名(OA号)
		String userName = getUserNameByToken(httpServletRequest).getData();

		// 2.判空
		if (null == datasetScope || "".equals(datasetScope.trim())) {
			datasetScope = DATA_SCOPE_PRIVATE;
		}
		// 3.业务逻辑操作
		String prefix = DatamartUtil.generateKey(datasetScope, userName);
		List<uploadFileBO> uploadFileBOlist = s3Service.listS3FolderInfo(bucketName, prefix);

		// 根据每个文件的s3key到数据库中查找ID,并设置到列表对应的文件中
		for (uploadFileBO uploadFileBO : uploadFileBOlist) {
			StringBuilder s3Key = new StringBuilder();
			s3Key.append("s3://").append(bucketName).append("/").append(uploadFileBO.getS3Key());
			// 存在数据库中的信息对应的entity
			S3UploadfileIndex s3UploadfileIndex = s3UploadfileIndexService.getOne(new QueryWrapper<S3UploadfileIndex>().eq("s3_key", s3Key.toString()));
			if (null != s3UploadfileIndex) {
				uploadFileBO.setId(s3UploadfileIndex.getId());
			}
		}
		if (null != uploadFileBOlist) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_LIST_SUCCESS));
			response.setData(uploadFileBOlist);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 文件删除
	 * 
	 * @param datasetScope       private或public
	 * @param fileName           文件名
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "本地上传文件删除", notes = "文件名")
	@RequestMapping(value = "delete", method = { RequestMethod.DELETE })
	@RequiresPermissions("localupload:delete")
	public ResultInfoDTO<Object> delete(@RequestParam(value = "datasetScope", required = false) String datasetScope, String fileName,
			HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		try {
			// 1.根据token获取用户名(OA号)
			String userName = getUserNameByToken(httpServletRequest).getData();

			// 2.判空
			if (null == fileName || "".equals(fileName.trim())) {
				response.setCode(ResultInfoDTO.ERROR);
				response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_DELETE_FAILED_FILENAME_EMPTY));
				return response;
			}
			if (null == datasetScope || "".equals(datasetScope)) {
				datasetScope = DATA_SCOPE_PRIVATE;
			}
			// 3.业务逻辑操作
			String key = DatamartUtil.generateKey(datasetScope, userName, fileName);
			// 删除S3文件
			Boolean flag = s3Service.deleteFileFromS3(bucketName, key);
			if (flag) {
				// 3.1.组装s3文件key
				StringBuilder s3key = new StringBuilder();
				s3key.append("s3://").append(bucketName).append("/").append(key);
				// 3.2.先查询存在性
				S3UploadfileIndex s3UploadfileIndex = s3UploadfileIndexService.getOne(new QueryWrapper<S3UploadfileIndex>().eq("s3_key", s3key.toString()));
				// 3.3.存在才删除，否则没必要执行删除操作
				boolean deleteFlag = false;
				if (null != s3UploadfileIndex) {
					deleteFlag = s3UploadfileIndexService.removeById(s3UploadfileIndex.getId());
				} else {
					deleteFlag = true;
				}
				// 3.4.返回逻辑
				if (deleteFlag) {
					response.setCode(ResultInfoDTO.OK);
					response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_DELETE_SUCCESS));
				} else {
					response.setCode(ResultInfoDTO.FAILED);
					response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_DELETE_SUCCESS_WHILE_DB_UPDATE_FAILED));
				}
			} else {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_DELETE_FAILED));
			}
			return response;
		} catch (Throwable e) {
			e.printStackTrace();
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage(e.toString());
			return response;
		}
	}

	/**
	 * 文件重命名
	 * 
	 * @param datasetScope       private或public
	 * @param sourceKey          源文件名
	 * @param targetKey          目标文件名
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "文件重命名", notes = "文件key,文件内容,范围")
	@RequestMapping(value = "rename", method = RequestMethod.PUT)
	@RequiresPermissions("localupload:rename")
	public ResultInfoDTO<Object> rename(@RequestParam(value = "datasetScope", required = false) String datasetScope, String sourceKey, String targetKey,
			HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1. 根据token获取用户名(OA号)
		String userName = getUserNameByToken(httpServletRequest).getData();

		// 2.判空
		if (sourceKey == null || "".equals(sourceKey.trim())) {
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_RENAME_FAILED_SOURCENAME_EMPTY));
			return response;
		}
		if (targetKey == null || "".equals(targetKey.trim())) {
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_RENAME_FAILED_TRAGETNAME_EMPTY));
			return response;
		}
		if (datasetScope == null || "".equals(datasetScope.trim())) {
			datasetScope = DATA_SCOPE_PRIVATE;
		}
		// 3.业务逻辑操作
		String sourceFileKey = DatamartUtil.generateKey(datasetScope, userName, sourceKey);
		String targetFileKey = DatamartUtil.generateKey(datasetScope, userName, targetKey);
		Boolean flag = s3Service.copyFile(bucketName, sourceFileKey, targetFileKey);
		if (flag) {
			// 3.1.组装s3原文件key、s3新文件key
			StringBuilder oldS3key = new StringBuilder();
			oldS3key.append("s3://").append(bucketName).append("/").append(sourceFileKey);
			StringBuilder newS3key = new StringBuilder();
			newS3key.append("s3://").append(bucketName).append("/").append(targetFileKey);
			// 3.2.先通过s3原文件key查询存在性
			S3UploadfileIndex entity = s3UploadfileIndexService.getOne(new QueryWrapper<S3UploadfileIndex>().eq("s3_key", oldS3key.toString()));
			boolean saveOrUpdateFlag = false;
			if (null == entity) {
				entity = new S3UploadfileIndex();
				entity.setS3Key(newS3key.toString());
				saveOrUpdateFlag = s3UploadfileIndexService.saveOne(entity);
			} else {
				// 目的条目存在，要作为旧的删除掉；然后执行更新操作
				S3UploadfileIndex targeteEntity = s3UploadfileIndexService.getOne(new QueryWrapper<S3UploadfileIndex>().eq("s3_key", newS3key.toString()));
				if (null != targeteEntity) {
					s3UploadfileIndexService.removeById(targeteEntity.getId());
				}
				entity.setS3Key(newS3key.toString());
				saveOrUpdateFlag = s3UploadfileIndexService.updateOne(entity);
			}

			if (saveOrUpdateFlag) {
				response.setCode(ResultInfoDTO.OK);
				response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_RENAME_SUCCESS));
			} else {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_RENAME_SUCCESS_WHILE_DB_UPDATE_FAILED));
			}

		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(LocalUploadCode.FILE_RENAME_FAILED));
		}
		return response;
	}

	/**
	 * 文件下载(弃用)
	 * 
	 * @param datasetScope
	 * @param fileName
	 * @param httpServletRequest
	 * @return
	 */
	@Deprecated
	@ApiOperation(value = "文件下载", notes = "文件key,范围")
	@RequestMapping(value = "download", method = { RequestMethod.GET })
	@RequiresPermissions("localupload:download")
	public ResultInfoDTO<Object> download(@RequestParam(value = "datasetScope", required = false) String datasetScope, String fileName,
			HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1. 根据token获取用户名(OA号)
		String userName = getUserNameByToken(httpServletRequest).getData();

		// 2.判空
		if (null == fileName || "".equals(fileName.trim())) {
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage("文件下载失败：文件名不能为空！");
			return response;
		}
		if (null == datasetScope || "".equals(datasetScope)) {
			datasetScope = DATA_SCOPE_PRIVATE;
		}
		// 3.业务逻辑操作
		String key = DatamartUtil.generateKey(datasetScope, userName, fileName);
		String result = s3Service.downS3File(bucketName, key);
		if (result != null) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage("文件下载成功！");
			response.setData(result);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage("文件下载失败！");
		}
		return response;
	}

	/**
	 * 文件编辑(不需要)
	 * 
	 * @param datasetScope
	 * @param fileName
	 * @param fileContent
	 * @param httpServletRequest
	 * @return
	 */
	@Deprecated
	@ApiOperation(value = "文件编辑", notes = "文件名,文件内容")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@RequiresPermissions("localupload:update")
	public ResultInfoDTO<Object> update(@RequestParam(value = "datasetScope", required = false) String datasetScope, String fileName, String fileContent,
			HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1. 根据token获取用户名(OA号)
		String userName = getUserNameByToken(httpServletRequest).getData();

		// 2.判空
		if (null == fileName || "".equals(fileName.trim())) {
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage("文件编辑失败：文件名不能为空！");
			return response;
		}
		if (null == fileContent || "".equals(fileContent)) {
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage("文件编辑失败：文件内容不能为空！");
			return response;
		}
		if (null == datasetScope || "".equals(datasetScope.trim())) {
			datasetScope = DATA_SCOPE_PRIVATE;
		}
		// 3.业务逻辑操作
		String result = s3Service.updateFileToS3(bucketName, DatamartUtil.createFolderPrefixUpdate(bucketName, datasetScope, userName), fileName, fileContent);
		if (result != null) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage("文件编辑完成！");
			response.setData(result);
		} else {
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage("文件编辑失败！");
		}
		return response;
	}

	/**
	 * 批量上传文件到s3桶(弃用)
	 * 
	 * @param files
	 * @param httpServletRequest
	 * @return
	 */
	@Deprecated
	@ApiOperation(value = "批量上传文件到s3桶", notes = "批量上传文件到s3桶")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "files", value = "文件数组", required = true, dataType = "MultipartFile[]") })
	@RequestMapping(value = "uploadFiles", method = { RequestMethod.POST })
	@RequiresPermissions("datamart:uploadFiles")
	public ResultInfoDTO<Object> uploadFiles(@RequestParam(value = "files", required = false) MultipartFile[] files, HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 根据token获取用户名(OA号)
		String userName = getUserNameByToken(httpServletRequest).getData();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				s3Service.uploadFileToS3(file, DatamartUtil.createS3PathKey(bucketName, DATA_SCOPE_PRIVATE, userName), bucketName);
			}
			response.setCode(ResultInfoDTO.OK);
			response.setMessage("批量上传文件到s3桶成功！");
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage("批量上传文件到s3桶失败：文件为空！");
		}
		return response;
	}
}
