package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.dao.zeppelin.mapper.SysZeppelinImageMapper;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: ZeppelinImageServiceImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-23 0:18
 */

@Service
public class ZeppelinImageServiceImpl implements ZeppelinImageService {
    @Autowired
    SysZeppelinImageMapper mapperDao;

    @Override
    public int insertZeppelinImage(SysZeppelinImage record) {
        return mapperDao.insertZeppelinImage(record);
    }

    @Override
    public int updateByPrimaryKey(SysZeppelinImage record) {
        return mapperDao.updateByPrimaryKey(record);
    }

    @Override
    public SysZeppelinImage getImagesByPrimaryKey(Integer id) {
        return mapperDao.selectByPrimaryKey(id);
    }

    @Override
    public List<SysZeppelinImage> getAllImage() {
        return  mapperDao.selectAllImage();
    }


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return mapperDao.deleteByPrimaryKey(id);
    }
}
