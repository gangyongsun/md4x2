package cn.com.goldwind.md4x.business.dao.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: SysZeppelinDaoImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-17 15:47
 */

@Repository
public class SysZeppelinDaoImpl extends BaseDaoImpl<SysZeppelin,Integer> implements SysZeppelinDao {
    @Autowired
    public SysZeppelinDaoImpl(@Qualifier("sqlSessionTemplateDefault") SqlSession sqlSession) {
        this.sqlSessionTemplate = sqlSession;
    }
}
