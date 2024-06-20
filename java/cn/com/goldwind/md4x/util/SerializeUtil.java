package cn.com.goldwind.md4x.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.sf.json.JSONObject;

/**
 * Java原生版的 Serialize
 * 
 */
public class SerializeUtil {
	private static final Class<?> CLAZZ = SerializeUtil.class;

	/**
	 * 序列化
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		byte[] bytes = null;
		if (null != object) {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos);) {
				oos.writeObject(object);
				bytes = baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
				LoggerUtils.fmtError(CLAZZ, e, "serialize error %s", JSONObject.fromObject(object));
			}
		} else {
			throw new NullPointerException("Can't serialize null");
		}
		return bytes;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @param requiredType
	 * @return
	 */
	public static <T> T deserialize(byte[] bytes, Class<T>... requiredType) {
		Object object = null;

		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bais);) {
			if (bytes != null) {
				object = ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.fmtError(CLAZZ, e, "serialize error %s", bytes);
		}
		return (T) object;
	}

	public static Object deserialize(byte[] bytes) {
		return deserialize(bytes, Object.class);
	}

}
