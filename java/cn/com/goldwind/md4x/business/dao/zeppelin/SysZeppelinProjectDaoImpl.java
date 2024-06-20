package cn.com.goldwind.md4x.business.dao.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: SysZeppelinProjectDaoImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-8-4 15:30
 */

@Repository
public class SysZeppelinProjectDaoImpl extends BaseDaoImpl<SysZeppelinLogs,Integer> implements SysZeppelinProjectDao {
    @Autowired
    public SysZeppelinProjectDaoImpl(@Qualifier("sqlSessionTemplateDefault") SqlSession sqlSession) {
        this.sqlSessionTemplate = sqlSession;
    }
}
