package cn.com.goldwind.md4x.shiro.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.shiro.DTO.LoginDTO;
import cn.com.goldwind.md4x.shiro.common.utils.TokenUtil;
import cn.com.goldwind.md4x.shiro.domain.entity.User;
import cn.com.goldwind.md4x.shiro.service.ITokenService;
import cn.com.goldwind.md4x.shiro.service.IUserService;
import cn.com.goldwind.md4x.util.MathUtil;
import cn.com.goldwind.md4x.util.StringUtils;
import cn.com.goldwind.md4x.util.i18n.LoginCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登录Controller
 * 
 * @author alvin
 *
 */
@Api(value = "登录接口", tags = { "登录接口" })
@RestController
@RequestMapping("sys")
public class LoginController extends BaseController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ITokenService tokenService;

	/**
	 * 登录
	 * 
	 * @param loginDTO
	 * @return
	 */
	@ApiOperation(value = "登陆", notes = "参数:用户名 密码")
	@PostMapping("login")
	public ResultInfoDTO<Object> login(@RequestBody LoginDTO loginDTO) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		response.setCode(ResultInfoDTO.ERROR);

		// ***********************基本信息验证***********************
		String userName = loginDTO.getUsername();
		String password = loginDTO.getPassword();
		if (StringUtils.isBlank(userName)) {
			response.setMessage(i18nService.getMessage(LoginCode.EMPTY_USERNAME_NOTIFY));
			return response;
		}
		if (StringUtils.isBlank(password)) {
			response.setMessage(i18nService.getMessage(LoginCode.EMPTY_PASSWORD_NOTIFY));
			return response;
		}

		// ***********************根据OA号查询用户是否在user表中***********************
		User user = userService.getOne(new QueryWrapper<User>().eq("user_name", userName));
		// ======================用户存在======================
		if (null != user) {
			// ======================用户没有被删除======================
			if (user.getDeleted()) {
				response.setCode(ResultInfoDTO.ERROR);
				response.setMessage(i18nService.getMessage(LoginCode.DELETED_USER_NOTIFY));
			} else {
				// ======================用户可用======================
				if (user.getAvailable()) {
					// -------------------金风员工OA认证-------------------
					// 认证标识
					boolean flag = false;
					if (user.getGoldwinder()) {
						// 1.OA认证
						flag = authLDAP(userName, password);
						/**
						 * 检查并按情况更新用户和用户组(部门)的关系<br>
						 * 针对于用户换部门的情况：备份之前的部门关系，新建新部门关系，清空拥有的前部门角色
						 */
						checkAndUpdateUserRoleGroupReleationship(user.getUserId());
					} else {
						String encryptPasswd = MathUtil.getMD5(password + user.getCredentialsSalt());
						// 2.用户名密码验证
						User nonGoldWindUser = userService.getOne(new QueryWrapper<User>().eq("user_name", userName).eq("passwd", encryptPasswd));
						if (null != nonGoldWindUser) {
							flag = true;
						}
					}
					if (flag) {
						// 认证成功，生成token，并保存到数据库
						Map<String, Object> tokenData = tokenService.createToken(user.getUserId());
						tokenData.put("userId", userName);
						tokenData.put("nickName", user.getNickName());

						// 设置返回对象
						response.setData(tokenData);
						response.setMessage(i18nService.getMessage(LoginCode.AUTH_SUCCESS));
						response.setCode(ResultInfoDTO.LOGIN_SUCCESS);

						// 设置用户最后登录时间并保存
						user.setLastLoginTime(new Date());
						userService.saveOrUpdate(user);
					} else {
						response.setMessage(i18nService.getMessage(LoginCode.AUTH_FAILED));
						response.setCode(ResultInfoDTO.ERROR);
					}
				} else {
					response.setMessage(i18nService.getMessage(LoginCode.VERIFYING));
					response.setCode(ResultInfoDTO.ERROR);
				}
			}
			return response;
		} else {
			response.setCode(ResultInfoDTO.ERROR);
			response.setMessage(i18nService.getMessage(LoginCode.NONE_REGISTRATION_NOTIFY));
			return response;
		}
	}

	/**
	 * 退出
	 * 
	 * @param token
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "登出", notes = "参数:token")
	@PostMapping("logout")
	public ResultInfoDTO<Object> logout(HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 参数里的token是swagger测试用，开发中用下面方法
		String token = TokenUtil.getRequestToken(httpServletRequest);
		tokenService.logout(token);
		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(LoginCode.LOGOUT_SUCCESS));
		return response;
	}

	/**
	 * 未登录，shiro应重定向到登录界面<br>
	 * 此处返回未登录状态信息由前端控制跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/un_auth", method = RequestMethod.GET)
	public ResultInfoDTO<Object> unAuth() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		response.setCode(ResultInfoDTO.ERROR);
		response.setMessage(i18nService.getMessage(LoginCode.NONE_LOGIN));
		return response;
	}

	/**
	 * 未授权，无权限 <br/>
	 * 此处返回未授权状态信息由前端控制跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
	public ResultInfoDTO<Object> unauthorized() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		response.setCode(ResultInfoDTO.ERROR);
		response.setMessage(i18nService.getMessage(LoginCode.NONE_PRIVILEGE));
		return response;
	}

}
