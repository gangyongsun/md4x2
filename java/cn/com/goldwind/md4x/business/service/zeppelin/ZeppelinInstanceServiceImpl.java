package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.bo.ZeppelinInfoVO;
import cn.com.goldwind.md4x.business.dao.zeppelin.mapper.SysZeppelinMapper;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: ZeppelinServiceImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-22 10:29
 */
@Service
public class ZeppelinInstanceServiceImpl implements ZeppelinInstanceService {
    @Autowired
    SysZeppelinMapper dao;

    @Override
    public int insertZeppelin(SysZeppelin record) {
        return dao.insertZeppelin(record);
    }

    @Override
    public int updateByUserName(SysZeppelin record) {
        return dao.updateByUserName(record);
    }

    @Override
    public int deleteByUserName(String UserName) {
        return dao.deleteByUserName(UserName);
    }


    @Override
    public SysZeppelin selectByUserName(String userName) {
        return dao.selectByUserName(userName);
    }

    @Override
    public ZeppelinInfoVO selectInfoByUserName(String userName) {
        return dao.selectInfoByUserName(userName);
    }

    @Override
    public SysZeppelin selectByPrimaryKey(Integer id) {
        return dao.selectByPrimaryKey(id);
    }

    @Override
    public SysZeppelin selectArnByUserName(String userName) {
        return dao.selectArnByUserName(userName);
    }

    @Override
    public List<SysZeppelin> selectAll() {
        return dao.selectAll();
    }
}
