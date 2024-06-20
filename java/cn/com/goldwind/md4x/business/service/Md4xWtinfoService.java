/**   
* @Title: TWfinfoService.java 
* @Package cn.com.goldwind.md4x.business.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 15, 2020 8:33:13 AM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.service;

import cn.com.goldwind.md4x.business.entity.datacenter.Md4xWtinfo;
import cn.com.goldwind.md4x.mybatis.Page;

/**
 * @ClassName: TWfinfoService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wangguiyu
 * @date Apr 15, 2020 8:33:13 AM
 *
 */
public interface Md4xWtinfoService {
	Page<Md4xWtinfo> getWtinfos(Integer pageNo, Integer pageSize, String wfid);
}
