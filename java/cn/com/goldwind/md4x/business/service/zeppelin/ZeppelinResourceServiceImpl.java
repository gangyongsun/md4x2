package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.dao.zeppelin.mapper.SysZeppelinResourceMapper;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: ZeppelinResourceServiceImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-30 15:35
 */
@Service
public class ZeppelinResourceServiceImpl extends ServiceImpl<SysZeppelinResourceMapper, SysZeppelinResource> implements ZeppelinResourceService {
    @Autowired
    SysZeppelinResourceMapper resourceMap;

    @Override
    public int insertResource(SysZeppelinResource record) {
        return resourceMap.insertResource(record);
    }

    @Override
    public int deleteResourceByid(Integer id) {
        return resourceMap.deleteResourceByid(id);
    }

    @Override
    public int deleteResourceByUserName(String userName) {
        return resourceMap.deleteResourceByUserName(userName);
    }

    @Override
    public int updateResourceById(SysZeppelinResource resource) {
        return resourceMap.updateResourceById(resource);
    }

    @Override
    public List<SysZeppelinResource> selectResourceByUserName(String userName) {
        return resourceMap.selectResourceByUserName(userName);
    }

    @Override
    public SysZeppelinResource selectResourceById(Integer id) {
        return resourceMap.selectResourceById(id);
    }

    @Override
    public int getCountResourceByUserName(SysZeppelinResource record) {
        return resourceMap.getCountResourceByUserName(record);
    }

    @Override
    public int getResourceId(String userName, String taskFlavor, Integer imageId) {
        return resourceMap.selectResourceId(userName,taskFlavor,imageId);
    }
}
