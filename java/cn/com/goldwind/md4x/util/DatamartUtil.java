package cn.com.goldwind.md4x.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;

/**
 * @Title: DatamartUtil.java
 * @Package cn.com.goldwind.md4x.util
 * @description 数据集公共类
 * @author 孙永刚更新，德阳创建
 * @date Aug 5, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public class DatamartUtil {

	public static Map<String, String> MTOM = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("2100", "2X00");
			put("2200", "2X00");
			put("2300", "2X00");
			put("2400", "2X00");
			put("2600", "2X00");
			put("2700", "2X00");
			put("2800", "2X00");
			put("2900", "2X00");
			put("3100", "3X00");
			put("3200", "3X00");
			put("3300", "3X00");
			put("3400", "3X00");
			put("3600", "3X00");
			put("3700", "3X00");
			put("3800", "3X00");
			put("3900", "3X00");
		}
	};

	/**
	 * 创建取数据集S3Path<br>
	 * 举例: s3://{ $bucket_name }/{ $private }/{ $oa }/{ $dataset_Id }/{$file_type }
	 * 
	 * @param bucketName   桶名
	 * @param datasetScope private或public
	 * @param datasetOwner 数据集owner
	 * @param datasetId    数据集ID
	 * @param fileType     文件类型
	 * @return
	 */
	public static String generateS3Path4DatasetExtract(String bucketName, String datasetScope, String datasetOwner, String datasetId, String fileType) {
		StringBuilder str = new StringBuilder();
		str.append("s3://").append(bucketName).append("/");
		str.append(datasetScope).append("/").append(datasetOwner).append("/");
		str.append(datasetId).append("/").append(fileType).append("/");
		return str.toString();
	}

	/**
	 * 规则:映射数据源位置<br>
	 * 举例:s3://{ $bucket_name }/batch_upload_parquet/
	 * 
	 * @param bucketName     桶名
	 * @param s3bucketPrefix 前缀
	 * @return
	 */
	public static String createS3Path(String bucketName, String s3bucketPrefix) {
		StringBuilder str = new StringBuilder();
		str.append("s3://").append(bucketName).append("/").append(s3bucketPrefix).append("/");
		return str.toString();
	}

	/**
	 * 个人规则：用户抽取结果集对应的catalog数据库：md4x_oa_db, 表名如：{ $oa_datasetId } true db catalog
	 * 数据库：md4x_private_${oa}, 表名如：md4x_private_${oa}_${dataSetId}
	 * 例：md4x_private_34453 ; md4x_private_34453_20200610084314664
	 * 
	 * @param bucketName   桶名
	 * @param datasetScope private或public
	 * @param datasetOwner 数据集owner
	 * @param datasetId    数据集ID
	 * @param isdb
	 * @return
	 */
	public static String createAthenaDBorTable(String bucketName, String datasetScope, String datasetOwner, String datasetId, boolean isdb) {
		StringBuilder str = new StringBuilder();
		str.append(bucketName).append(datasetScope).append("_").append(datasetOwner);
		if (!isdb) {
			str.append("_");
			str.append(datasetId);
		}
		return str.toString();
	}

	/**
	 * 将列表元素中的"."替换成下划线
	 * 
	 * @param list
	 * @return
	 */
	public static List<MainField> dealWithList(List<MainField> list) {
		if (null != list && list.size() > 0) {
			for (MainField mainField : list) {
				if (mainField != null && mainField.getModelEntryEN() != null) {
					mainField.setCustomModelEntryEN(mainField.getModelEntryEN().replaceAll("\\.", "_"));
					mainField.setCondition(mainField.getCondition() == null ? "" : mainField.getCondition());
					mainField.setConditionValue(mainField.getConditionValue() == null ? 0 : mainField.getConditionValue());
				}
			}
		}
		return list;
	}

	/**
	 * 创建S3上传文件的路径,比如完整的key是：s3://md4x-platform/private/50508/upload/test.txt<br>
	 * 这里创建的是路径：md4x-platform/private/50508/upload
	 * 
	 * @param bucketName   桶名
	 * @param datasetScope private或public
	 * @param datasetOwner 数据集owner
	 * @return S3文件路径
	 */
	public static String createS3PathKey(String bucketName, String datasetScope, String datasetOwner) {
		StringBuilder str = new StringBuilder();
		// 生成pathKey
		String pathKey = generateKey(datasetScope, datasetOwner);
		str.append(bucketName).append("/").append(pathKey);
		return str.toString();
	}

	/**
	 * 规则：用户配置文件{ $bucketName }/{ $private }/{ $oa }/upload
	 * 
	 * @param bucketName   桶名
	 * @param datasetScope private或public
	 * @param datasetOwner 数据集owner
	 * @return
	 */
	public static String createFolderPrefixUpdate(String bucketName, String datasetScope, String datasetOwner) {
		StringBuilder str = new StringBuilder();
		// 生成pathKey
		String pathKey = generateKey(datasetScope, datasetOwner);
		str.append(bucketName).append("/").append(pathKey);
		return str.toString();
	}

	/**
	 * 生成S3文件路径key<br>
	 * 例如：private/50508/upload
	 * 
	 * @param datasetScope private或public
	 * @param datasetOwner 数据集owner
	 * @return
	 */
	public static String generateKey(String datasetScope, String datasetOwner) {
		StringBuilder str = new StringBuilder();
		str.append(datasetScope).append("/").append(datasetOwner).append("/upload");
		return str.toString();
	}

	/**
	 * 生成S3文件key<br>
	 * 例如：private/50508/upload/meeting.txt
	 * 
	 * @param datasetScope private或public
	 * @param datasetOwner 数据集owner
	 * @param fileName     文件名
	 * @return
	 */
	public static String generateKey(String datasetScope, String datasetOwner, String fileName) {
		StringBuilder str = new StringBuilder();
		// 生成pathKey
		String pathKey = generateKey(datasetScope, datasetOwner);
		str.append(pathKey).append("/").append(fileName);
		return str.toString();
	}

}
