package cn.com.goldwind.md4x.util;

import cn.com.goldwind.md4x.business.bo.download.DownloadFileVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TreeUtil
 * @Description: 获取树状结构
 * @Author: yaleiwang
 * @Date: 2020-10-14 21:13
 */
public class TreeUtil {
    public static List<DownloadFileVO> getTree(List<DownloadFileVO> list) {
        List<DownloadFileVO> rootList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            //声明一个map，用来过滤已操作过的数据
            Map<String, String> map = Maps.newHashMapWithExpectedSize(list.size());
            for(DownloadFileVO beanTree:list){
                if("".equals(beanTree.getPid()) || null==beanTree.getPid()){
                    getChild(beanTree, map,list);
                    rootList.add(beanTree);
                }
            }
        }
        return rootList;
    }

    private static void getChild(DownloadFileVO beanTree, Map<String, String> map,List<DownloadFileVO> list) {
        List<DownloadFileVO> childList = Lists.newArrayList();
        list.stream().filter(c -> !map.containsKey(c.getFileName())).filter(c -> c.getPid().equals(beanTree.getFileName())).forEach(c -> {
            map.put(c.getFileName(), c.getPid());
            getChild(c, map,list);
            childList.add(c);
        });
        beanTree.setChildren(childList);
    }
}
