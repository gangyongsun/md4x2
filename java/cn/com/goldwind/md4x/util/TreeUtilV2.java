package cn.com.goldwind.md4x.util;

import cn.com.goldwind.md4x.business.bo.download.DownloadFileVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TreeUtilV2
 * @Description: 获取树状结构
 * @Author: yaleiwang
 * @Date: 2020-10-14 21:32
 */
public class TreeUtilV2 {
    public static List<DownloadFileVO> RecursiveAddress(List<DownloadFileVO> treeNodes){
        List<DownloadFileVO> trees = new ArrayList<DownloadFileVO>();
        for (DownloadFileVO treeNode : treeNodes) {
            if ("".equals(treeNode.getPid()) || null==treeNode.getPid()) {
                trees.add(findAddressChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找地址子节点
     * @param treeNodes
     * @return
     */
    private static DownloadFileVO findAddressChildren(DownloadFileVO treeNode,List<DownloadFileVO> treeNodes) {
        for (DownloadFileVO it : treeNodes) {
            if(treeNode.getFileName().equals(it.getPid())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<DownloadFileVO>());
                }

                treeNode.getChildren().add(findAddressChildren(it,treeNodes));
            }
        }
        return treeNode;
    }
}
