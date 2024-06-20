package cn.com.goldwind.md4x.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO流工具类
 * 
 * @author alvin
 * 
 */
public class IoUtil {
	private final static Class<? extends Object> SELF = IoUtil.class;

	/**
	 * 关闭一个或多个流对象
	 * 
	 * @param closeables 可关闭的流对象列表
	 * @throws IOException
	 */
	public static void close(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			try {
				if (closeable != null) {
					closeable.close();
					closeable = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				LoggerUtils.error(SELF, "关闭流失败！", e);
			}
		}
	}

}
