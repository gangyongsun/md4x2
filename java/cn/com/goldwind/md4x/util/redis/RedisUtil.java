package cn.com.goldwind.md4x.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.goldwind.md4x.util.StringUtils;
import lombok.Data;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Redis Utils
 * 
 * @author alvin
 *
 */
@Component
@Data
public class RedisUtil {

	@Autowired
	private JedisPool jedisPool;

	/**
	 * 从Redis池获取Redis对象
	 * 
	 * @return
	 */
	public Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (JedisConnectionException e) {
			e.printStackTrace();
			String message = StringUtils.trim(e.getMessage());
			if ("Could not get a resource from the pool".equalsIgnoreCase(message)) {
				System.out.println("++++++++++请检查是否安装并启动redis服务++++++++");
				System.out.println("|①.请检查redis启动是否带配置文件启动，也就是是否有密码，是否端口有变化（默认6379）|");
				System.out.println("项目退出中....生产环境中，请删除这些东西");
				System.exit(0);// 停止项目
			}
			throw new JedisConnectionException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return jedis;
	}

	/**
	 * 关闭连接
	 * 
	 * @param jedis
	 */
	public void disconnect(Jedis jedis) {
		jedis.disconnect();
	}

	/**
	 * 将jedis 返还连接池
	 * 
	 * @param jedis
	 */
	public void returnResource(Jedis jedis) {
		if (null != jedis) {
			jedis.close();
			jedis = null;
		}
	}

}
