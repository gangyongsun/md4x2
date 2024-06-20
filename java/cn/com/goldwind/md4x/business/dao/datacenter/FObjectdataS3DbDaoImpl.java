/**   
* @Title: F7scadaMd4xDbDao.java 
* @Package cn.com.goldwind.md4x.business.dao.datacensus 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 26, 2020 1:08:02 PM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.dao.datacenter;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.com.goldwind.md4x.business.entity.datacenter.FObjectdataS3Db;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;

/**
 * @ClassName: FObjectdataS3DbDaoImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wangguiyu
 * @date Apr 26, 2020 1:08:02 PM
 *
 */
@Repository
public class FObjectdataS3DbDaoImpl extends BaseDaoImpl<FObjectdataS3Db, String> implements FObjectdataS3DbDao {
	@Autowired
	public FObjectdataS3DbDaoImpl(@Qualifier("sqlSessionTemplateDataCenter") SqlSession sqlSession) {
		this.sqlSessionTemplate = sqlSession;
	}

}
