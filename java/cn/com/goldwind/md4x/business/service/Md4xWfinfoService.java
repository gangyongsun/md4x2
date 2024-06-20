/**   
* @Title: TWfinfoService.java 
* @Package cn.com.goldwind.md4x.business.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 15, 2020 8:33:13 AM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.service;

import java.util.List;

import cn.com.goldwind.md4x.business.bo.Md4xWfinfoSummaryVO;
import cn.com.goldwind.md4x.business.bo.WindFarmInfo;
import cn.com.goldwind.md4x.business.bo.WindFarmInfoVO;
import cn.com.goldwind.md4x.business.bo.WindFarmOwnerInfo;
import cn.com.goldwind.md4x.business.bo.WindFarmTypeInfo;
import cn.com.goldwind.md4x.mybatis.Page;

/**
 * @ClassName: TWfinfoService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wangguiyu
 * @date Apr 15, 2020 8:33:13 AM
 *
 */
public interface Md4xWfinfoService {
	List<Md4xWfinfoSummaryVO> getMd4xWfinfoSummary(String searchKey, String searchValue);
	List<WindFarmInfo> getAllFarmInfos();
	List<WindFarmOwnerInfo>getAllOwners();
	List<WindFarmTypeInfo> getAllFarmType();//风场风机类型
	Page<WindFarmInfoVO> getWindFarminfo(Integer pageNo, Integer pageSize, String searchKey,String searchValue,String province,String sortFlag);
}
