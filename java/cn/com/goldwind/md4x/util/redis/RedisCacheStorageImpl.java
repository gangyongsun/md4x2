package cn.com.goldwind.md4x.util.redis;

import cn.com.goldwind.md4x.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.Map;


@Service("redisCacheStorage")
public class RedisCacheStorageImpl<V> implements RedisCacheStorage<String, V> {
	private Logger log = LoggerFactory.getLogger(RedisCacheStorageImpl.class);

	/**
	 * 默认过时时间
	 */
	private static final int EXPRIE_TIME = 3600 * 24;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public boolean set(String key, V value) {
		return set(key, value, EXPRIE_TIME);
	}

	@Override
	public boolean set(String key, V value, int exp) {
		boolean flag = false;
		if (!StringUtils.isEmpty(key)) {
			Jedis jedis = null;
			try {
				jedis = redisUtil.getJedis();
				// 序列化对象后插入到redis
				// 我们需要使用 public String setex(byte[] key, int seconds, byte[]
				// value)，所以将key.getBytes()
				jedis.setex(key.getBytes(), exp, SerializeUtil.serialize(value));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					redisUtil.returnResource(jedis);
				}
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public V get(String key, Object object) {
		V v = null;
		if (!StringUtils.isEmpty(key)) {
			Jedis jedis = null;
			try {
				jedis = redisUtil.getJedis();
				if (jedis.exists(key.getBytes())) {
					// 我们存入的时候使用的是key.getBytes(),所以取的时候也要使用它的key数组
					byte valueByte[] = jedis.get(key.getBytes()); // 从redis得到值
					if (valueByte.length > 0) {
						// 反序列化取出我们的数据
						v = (V) SerializeUtil.deserialize(valueByte); // 将值转换为我们插入redis之前的数据类型
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					redisUtil.returnResource(jedis);
				}
			}
		} else {
			log.info("redis取值，key为空");
		}
		return v;
	}

	@Override
	public boolean remove(String key) {
		boolean flag = false;
		if (!StringUtils.isEmpty(key)) {
			Jedis jedis = null;
			try {
				jedis = redisUtil.getJedis();
				jedis.del(key.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					redisUtil.returnResource(jedis);
				}
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public boolean hset(String cacheKey, String key, V value) {
		boolean flag = false;
		if (!StringUtils.isEmpty(cacheKey)) {
			Jedis jedis = null;
			try {
				jedis = redisUtil.getJedis();
				byte valueData[] = SerializeUtil.serialize(value);
				// 执行插入哈希
				// public Long hset(byte[] key, byte[] field, byte[] value)
				jedis.hset(cacheKey.getBytes(), key.getBytes(), valueData);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					redisUtil.returnResource(jedis);
				}
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public V hget(String cacheKey, String key, Object object) {
		V v = null;
		if (cacheKey.getBytes().length > 0) {
			Jedis jedis = null;
			try {
				jedis = redisUtil.getJedis();
				// 执行查询
				byte valueDate[] = jedis.hget(cacheKey.getBytes(), key.getBytes());
				// 判断值是否非空
				if (valueDate.length > 0) {
					// 反序列化拿到数据
					v = (V) SerializeUtil.deserialize(valueDate);
				}
			} catch (JedisException e) {
				e.printStackTrace();
			} finally {
				redisUtil.returnResource(jedis);
			}
		}
		return v;
	}

	@Override
	public Map<String, V> hget(String cacheKey, Object object) {
		Map<String, V> result = new HashMap<String, V>();
		if (!StringUtils.isEmpty(cacheKey)) {
			Jedis jedis = null;
			try {
				jedis = redisUtil.getJedis();
				// 获取列表集合 因为插入redis的时候是key和value都是字节数组，所以返回的结果也是字节数组
				Map<byte[], byte[]> map = jedis.hgetAll(cacheKey.getBytes());

				if (null != map) {
					for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
						result.put(new String(entry.getKey()), (V) SerializeUtil.deserialize(entry.getValue()));
					}
				}
			} catch (JedisException e) {
				e.printStackTrace();
			} finally {
				redisUtil.returnResource(jedis);
			}
		}
		return result;
	}

}