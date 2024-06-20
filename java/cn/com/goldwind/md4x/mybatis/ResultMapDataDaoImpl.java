package cn.com.goldwind.md4x.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ResultMapDataDaoImpl extends BaseDaoImpl<ResultMapData, String> implements ResultMapDataDao {

	@Autowired
	public ResultMapDataDaoImpl(@Qualifier("sqlSessionTemplateDataCenter") SqlSession sqlSession) {
		this.sqlSessionTemplate = sqlSession;
	}



}