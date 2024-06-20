package cn.com.goldwind.md4x.business.dao.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinResource;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: SysZeppelinResourceDaoImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-30 14:51
 */
@Repository
public class SysZeppelinResourceDaoImpl extends BaseDaoImpl<SysZeppelinResource,Integer> implements SysZeppelinResourceDao {
    @Autowired
    public SysZeppelinResourceDaoImpl(@Qualifier("sqlSessionTemplateDefault") SqlSession sqlSession) {
        this.sqlSessionTemplate = sqlSession;
    }
}
