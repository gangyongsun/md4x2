
package cn.com.goldwind.md4x.business.dao.datacenter;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.com.goldwind.md4x.business.entity.datacenter.Md4xWfinfo;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;

/**
 * @ClassName: 
 * @Description: 风场
 * @author wangguiyu
 * @date Apr 10, 2020 4:52:52 PM 
 *
 */

@Repository
public class Md4xWfinfoDaoImpl extends BaseDaoImpl<Md4xWfinfo,String> implements Md4xWfinfoDao  {
	
	@Autowired
	public Md4xWfinfoDaoImpl(@Qualifier("sqlSessionTemplateDataCenter") SqlSession sqlSession) {
		this.sqlSessionTemplate = sqlSession;
	}
}
