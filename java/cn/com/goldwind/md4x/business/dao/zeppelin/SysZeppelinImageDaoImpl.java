package cn.com.goldwind.md4x.business.dao.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage;
import cn.com.goldwind.md4x.mybatis.BaseDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: SysZeppelinImageImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-21 21:02
 */

@Repository
public class SysZeppelinImageDaoImpl extends BaseDaoImpl<SysZeppelinImage,Integer> implements SysZeppelinImageDao {
    @Autowired
    public SysZeppelinImageDaoImpl(@Qualifier("sqlSessionTemplateDefault") SqlSession sqlSession) {
        this.sqlSessionTemplate = sqlSession;
    }
}
