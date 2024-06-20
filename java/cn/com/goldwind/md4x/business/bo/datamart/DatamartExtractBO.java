package cn.com.goldwind.md4x.business.bo.datamart;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 
 * @Title: DatamartExtractBO.java
 * @Package cn.com.goldwind.md4x.business.bo
 * @description 数据集抽条件业务类，前端传输变量列表到后端的类
 * @author 孙永刚修改，秦德阳创建
 * @date Aug 5, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class DatamartExtractBO implements Serializable {
	private static final long serialVersionUID = 1872439263643755984L;

	/**
	 * 风场ID
	 */
	private String wfid;

	/**
	 * 文件类型
	 */
	private String file_type;

	/**
	 * 开始时间
	 */
	private String begin_time;

	/**
	 * 结束时间
	 */
	private String end_time;

	/**
	 * 风机id数组
	 */
	private List<String> wtids;
	
	/**
	 * 协议列表
	 */
	private List<Integer> protocolIds;

	/**
	 * 风机类型数组
	 */
	private List<String> wtypes;

}
