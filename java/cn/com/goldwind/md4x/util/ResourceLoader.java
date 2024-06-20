package cn.com.goldwind.md4x.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源文件加载器
 * 
 * @author Bill (Alvin updated)
 * @date Jul 13, 2020
 */
public final class ResourceLoader {
	private final static Class<? extends Object> SELF = FileUtil.class;

	private static Map<String, Properties> map = new ConcurrentHashMap<String, Properties>();

	/**
	 * 获取properties对象
	 * 
	 * @param fileName 配置文件名
	 * @return properties对象
	 */
	public static Properties getPropertiesObject(String fileName) {
		Properties properties = map.get(fileName);
		try (
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
		) {
			if (null == properties) {
				// 创建Properties对象
				properties = new Properties();
				properties.load(br);
				// 放到map
				map.put(fileName, properties);
			}
		} catch (IOException e) {
			e.printStackTrace();
			LoggerUtils.error(SELF, "IO错误！", e);
		}
		return properties;
	}

	/**
	 * 获取properties对象
	 * 
	 * @param filePath 配置文件路径
	 * @param fileName 配置文件名
	 * @return properties对象
	 */
	public static Properties getPropertiesObject(String filePath, String fileName) {
		filePath = FileUtil.setPathEndWithSlash(filePath);
		Properties properties = new Properties();
		try(
				InputStream is = new FileInputStream(filePath + fileName);
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
		) {
			properties.load(br);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LoggerUtils.error(SELF, "文件未找到！", e);
		} catch (IOException e) {
			e.printStackTrace();
			LoggerUtils.error(SELF, "IO错误！", e);
		}
		return properties;
	}

}
