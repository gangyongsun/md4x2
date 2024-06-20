package cn.com.goldwind.md4x.business.dao.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: SysZeppelinLogsDaoImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-19 21:09
 */
@Repository
public class SysZeppelinLogsDaoImpl extends BaseDaoImpl<SysZeppelinLogs,Integer> implements SysZeppelinLogsDao {
    @Autowired
    public SysZeppelinLogsDaoImpl(@Qualifier("sqlSessionTemplateDefault") SqlSession sqlSession) {
        this.sqlSessionTemplate = sqlSession;
    }
}
