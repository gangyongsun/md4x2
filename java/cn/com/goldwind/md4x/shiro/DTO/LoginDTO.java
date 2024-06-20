package cn.com.goldwind.md4x.shiro.DTO;

import lombok.Data;

/**
 * Token验证，登录用户的输入
 * @author alvin
 *
 */
@Data
public class LoginDTO {
    private String username;
    private String password;
}
