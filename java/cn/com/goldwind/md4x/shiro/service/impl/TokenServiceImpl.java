package cn.com.goldwind.md4x.shiro.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.shiro.auth.TokenGenerator;
import cn.com.goldwind.md4x.shiro.common.utils.TokenUtil;
import cn.com.goldwind.md4x.shiro.domain.entity.SysToken;
import cn.com.goldwind.md4x.shiro.domain.mapper.SysTokenMapper;
import cn.com.goldwind.md4x.shiro.service.ITokenService;
import cn.com.goldwind.md4x.util.DateUtil;

/**
 * 
 * @author alvin
 *
 */
@Service
public class TokenServiceImpl extends ServiceImpl<SysTokenMapper, SysToken> implements ITokenService {

	@Autowired
	private SysTokenMapper sysTokenMapper;

	@Override
	/**
	 * 生成一个token
	 * 
	 * @param [userId]
	 * @return Result
	 */
	public Map<String, Object> createToken(Integer userId) {
		Map<String, Object> result = new HashMap<>();
		// 生成一个token
		String token = TokenGenerator.generateValue();

		// 当前时间
		Date nowTime = new Date();
		// 过期时间
		Date expireTime = DateUtil.addMinutes(nowTime, TokenUtil.EXPIRE);
		// 判断是否生成过token
		SysToken tokenEntity = baseMapper.selectOne(new LambdaQueryWrapper<SysToken>().eq(SysToken::getUserId, userId));
		if (tokenEntity == null) {
			tokenEntity = new SysToken();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(nowTime);
			tokenEntity.setExpireTime(expireTime);
			// 保存token
			sysTokenMapper.insert(tokenEntity);
		} else {
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(nowTime);
			tokenEntity.setExpireTime(expireTime);
			// 更新token
			sysTokenMapper.updateById(tokenEntity);
		}
		result.put("token", token);
		result.put("expire", TokenUtil.EXPIRE);
		return result;
	}

	@Override
	public void logout(String token) {
		SysToken byeToken = findByToken(token);
		// 生成一个token
		token = TokenGenerator.generateValue();
		// 修改token
		byeToken.setToken(token);
		// 当前时间
		Date nowTime = new Date();
		byeToken.setUpdateTime(nowTime);
		byeToken.setExpireTime(nowTime);
		sysTokenMapper.updateById(byeToken);
	}

	@Override
	public SysToken findByToken(String accessToken) {
		return baseMapper.selectOne(new LambdaQueryWrapper<SysToken>().eq(SysToken::getToken, accessToken));
	}

	@Override
	public boolean expireTokensByPermissionId(Integer permissionId) {
		int result = sysTokenMapper.expireTokensByPermissionId(permissionId);
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean expireTokensByRoleId(Integer roleId) {
		int result = sysTokenMapper.expireTokensByRoleId(roleId);
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

}
