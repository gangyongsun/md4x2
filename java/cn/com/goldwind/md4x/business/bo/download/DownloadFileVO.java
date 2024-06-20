package cn.com.goldwind.md4x.business.bo.download;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: downloadFileBO
 * @Description: 下载文件信息
 * @Author: yaleiwang
 * @Date: 2020-9-20 23:58
 */
@Data
public class DownloadFileVO implements Serializable {
    private static final long serialVersionUID = 5914269108482075080L;

    /**
     * 是否文件夹
     */
    private boolean isFolder;
    /*
     * 文件名称
     */
    private String fileName;

    /*
     * 文件key
     */
    private String fileKey;

    /**
     * 最近更新时间
     */
    private String fileLastModified;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 父id
     */
    private String pid;
    /**
     * 下一级文件列表
     */
    private List<DownloadFileVO> children;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;  //先判断o是否为本对象，this 指向当前的对象
        if (o == null || getClass() != o.getClass())
            return false; //再判断o是否为null，和o.类对象和本类对象是否一致
        DownloadFileVO obj = (DownloadFileVO) o;  //再把o对象强制转化为User类对象

        return  Objects.equals(fileName, obj.fileName);//查看两个对象的name和sex属性值是否相等
    }
}
