/**   
* @Title: F7scadaMd4XDbService.java 
* @Package cn.com.goldwind.md4x.business.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 26, 2020 1:15:02 PM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.service;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName: F7scadaMd4XDbService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author wangguiyu
 * @date Apr 26, 2020 1:15:02 PM 
 *
 */
public interface AwsTransRecordService {
	 public Map<String,Object>  getDataIntegrity(String dataType,Date startDate,Date endDate,String wfid,String wtids);
	 public Map<String,Double> calculateDataIntegrity(Date startDate,Date endDate);

}
