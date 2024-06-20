
package cn.com.goldwind.md4x.business.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.com.goldwind.md4x.business.entity.datamart.SysOperationLogs;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;

/**
 * @ClassName: 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author wangguiyu
 * @date Apr 10, 2020 4:52:52 PM 
 *
 */

@Repository
public class SysOperationLogsDaoImpl extends BaseDaoImpl<SysOperationLogs,Integer> implements SysOperationLogsDao  {
	
	@Autowired
	public SysOperationLogsDaoImpl(@Qualifier("sqlSessionTemplateDefault") SqlSession sqlSession) {
		this.sqlSessionTemplate = sqlSession;
	}
}
