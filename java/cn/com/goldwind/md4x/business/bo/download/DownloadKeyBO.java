package cn.com.goldwind.md4x.business.bo.download;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: DownloadKeyBO
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-9-22 16:29
 */
@Data
public class DownloadKeyBO {
    private Integer id;

    private List<String> keys;
}
