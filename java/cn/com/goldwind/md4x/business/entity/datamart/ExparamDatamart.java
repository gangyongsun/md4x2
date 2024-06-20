package cn.com.goldwind.md4x.business.entity.datamart;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @Title: ExparamDatamart.java
 * @Package cn.com.goldwind.md4x.business.entity.datamart
 * @description 抽取数据集类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 4, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_exparam_datamart")
public class ExparamDatamart implements Serializable {
	private static final long serialVersionUID = 1773986256949235740L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@TableField("user_name")
	private String userName;

	@TableField("datamart_name")
	private String datamartName;

	@TableField("wfid")
	private String wfid;

	@TableField("wtid")
	//@TableField(el = "wtid,typeHandler = cn.com.goldwind.md4x.util.handler.AliJsonTypeHandler")
    private Object wtid;

	@TableField("wfcount")
	private Integer wfcount;

	@TableField("s3_path")
	private String s3Path;

	@TableField("athena_path")
	private String athenaPath;

	@TableField("athena_db")
	private String athenaDb;

	@TableField("athena_table")
	private String athenaTable;

	@TableField("file_size")
	private String fileSize;

	@TableField("operation_status")
	private Integer operationStatus;

	@TableField("is_del")
	private Integer isDel;

	@TableField("dm_status")
	private Integer dmStatus;

	@TableField("ext_status")
	private Integer extStatus;

	@TableField("file_type")
	private String fileType;

	@TableField("begin_time")
	private Date beginTime;

	@TableField("end_time")
	private Date endTime;

	@TableField("param_content")
	private Object paramContent;

	@TableField("remarks")
	private String remarks;

	@TableField("create_time")
	private Date createTime;

	@TableField("update_time")
	private Date updateTime;

	@TableField("dataset_id")
	private String datasetId;

	@TableField("dataset_scope")
	private String datasetScope;

	@TableField("dataset_owner")
	private String datasetOwner;

	@TableField("bucket_name")
	private String bucketName;

	@TableField("wf_name")
	private String wfName;

	@TableField("data_integrity")
	private String dataIntegrity;

	@TableField("variable_num")
	private Integer variableNum;
	
	@TableField("data_extracting_starttime")
	private Date dataExtractingStarttime;
	
	@TableField("data_extracting_endtime")
	private Date dataExtractingEndtime;

}