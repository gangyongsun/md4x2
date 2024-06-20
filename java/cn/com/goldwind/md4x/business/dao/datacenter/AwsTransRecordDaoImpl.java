
package cn.com.goldwind.md4x.business.dao.datacenter;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.com.goldwind.md4x.business.entity.datacenter.AwsTransRecord;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;

/**
 * @ClassName:  AwsTransRecordDao 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author wangguiyu
 * @date Apr 26, 2020 1:08:02 PM 
 *
 */
@Repository
public class AwsTransRecordDaoImpl extends BaseDaoImpl<AwsTransRecord,String> implements AwsTransRecordDao   {
	@Autowired
	public  AwsTransRecordDaoImpl(@Qualifier("sqlSessionTemplateDataCenter") SqlSession sqlSession) {
		this.sqlSessionTemplate = sqlSession;
	}

}
