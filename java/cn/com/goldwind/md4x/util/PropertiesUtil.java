package cn.com.goldwind.md4x.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源文件操作类
 * 
 * @author Bill (Alvin updated)
 * @date Aug 15, 2017
 */
public class PropertiesUtil {
	private final static Class<? extends Object> SELF = PropertiesUtil.class;

	private static Map<String, String> configMap = new ConcurrentHashMap<String, String>();

	/**
	 * 根据key获取值
	 * 
	 * @param key
	 * @param fileName 配置文件名
	 * @return
	 */
	public static String getValueByKey(String key, String fileName) {
		try {
			key = key.trim();

			Properties prop = ResourceLoader.getPropertiesObject(fileName);
			String value = prop.getProperty(key);// 配置文件中获取的value
			
			updateConfigMap(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.error(SELF, "获取值失败！", e);
		}
		return configMap.get(key);
	}

	/**
	 * 根据key获取值
	 * 
	 * @param key
	 * @param filePath
	 * @param fileName 配置文件名
	 * @return
	 */
	public static String getValueByKey(String key, String filePath, String fileName) {
		try {
			key = key.trim();
			
			Properties prop = ResourceLoader.getPropertiesObject(filePath, fileName);
			String value = prop.getProperty(key);// 配置文件中获取的value
			
			updateConfigMap(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.error(SELF, "获取值失败！", e);
		}
		return configMap.get(key);
	}

	/**
	 * 更新configMap
	 * 
	 * @param key   键
	 * @param value 值
	 */
	private static void updateConfigMap(String key, String value) {
		if (null != value) {
			// 判断map中是否存在这个key，存在直接取，否则先放再取
			if (!configMap.containsKey(key)) {
				configMap.put(key, value);
			} else {
				// 如果configMap里的值和它不同，则替换掉configMap里的值
				if (!configMap.get(key).equals(value)) {
					configMap.replace(key, value);
				}
			}
		} else {
			configMap.remove(key);
			LoggerUtils.info(SELF, "配置文件中删除了该配置，configMap中也同步删除该配置:" + key);
		}
	}

	/**
	 * 获取properties键值对列表
	 * 
	 * @param propertie
	 * @return
	 */
	public static Set<Entry<Object, Object>> getEntrySet(String filePath, String fileName) {
		Properties propertie = getAllParams(filePath, fileName);
		return propertie.entrySet();
	}

	public static Properties getAllParams(String filePath, String fileName) {
		return ResourceLoader.getPropertiesObject(filePath, fileName);
	}

}
