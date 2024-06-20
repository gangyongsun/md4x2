package cn.com.goldwind.md4x.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要对StringUtils的一些方法进行重写，以达到更方便的使用的目的
 * 
 * @author alvin
 *
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
	// =========================对象为null或空或字符串"null"的判断逻辑方法：start=========================
	/**
	 * 判断多个对象中是否有存在[null、空或者值为字符串"null"]的
	 * 
	 * @param objects
	 * @return 只要有一个元素为[null、空或者值为字符串"null"]，则返回true
	 */
	public static boolean isBlank(Object... objects) {
		Boolean result = false;
		for (Object object : objects) {
			if (null == object || "".equals(object.toString().trim()) || "null".equals(object.toString().trim())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 一次性判断多个或单个对象不为空
	 * 
	 * @param objects
	 * @return 只要有一个元素不为Blank，则返回true
	 */
	public static boolean isNotBlank(Object... objects) {
		return !isBlank(objects);
	}

	public static boolean isBlank(String... strs) {
		Object[] object = strs;
		return isBlank(object);
	}

	public static boolean isNotBlank(String... strs) {
		return !isBlank(strs);
	}
	// =========================对象为null或空或字符串"null"的判断逻辑方法：end=========================

	// =========================对象为null或空的判断逻辑方法：start=========================
	/**
	 * 判断多个对象中是否有存在[null或空]的
	 * 
	 * @param objects
	 * @return 只要有一个元素为[null或空]，则返回true
	 */
	public static boolean isNullOrEmpty(Object... objects) {
		Boolean result = false;
		for (Object object : objects) {
			if (null == object || "".equals(object.toString().trim())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 判断多个对象是否有不为[null或空]的
	 * 
	 * @param objects
	 * @return 只要有一个对象不为[null或空]，则返回true
	 */
	public static boolean isNotNullOrEmpty(Object... objects) {
		return !isNullOrEmpty(objects);
	}

	public static boolean isNullOrEmpty(String... strs) {
		Object[] object = strs;
		return isNullOrEmpty(object);
	}

	public static boolean isNotNullOrEmpty(String... strs) {
		return !isNullOrEmpty(strs);
	}
	// =========================对象为null或空的判断逻辑方法：end=========================

	public static String trimToEmpty(Object object) {
		return isBlank(object) ? "" : object.toString().trim();
	}

	/**
	 * 获取随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandom(int length) {
		String result = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				result += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				// 数字
				result += String.valueOf(random.nextInt(10));
			}
		}
		return result;
	}

	/**
	 * 判断一个字符串在数组中存在几个
	 * 
	 * @param target   要匹配的字符串
	 * @param strArray 字符串数组
	 * @return
	 */
	public static int countStrInArray(String target, String[] strArray) {
		int i = 0;
		if (null != target && target.length() != 0 && null != strArray) {
			for (String string : strArray) {
				boolean result = target.equals(string);
				i = result ? ++i : i;
			}
		}
		return i;
	}

	/**
	 * 判断一个字符串是否为JSON对象
	 * 
	 * @param str
	 * @return 是返回该JSON对象，不是则返回null
	 */
	public static JSONObject parse2JsonObject(String str) {
		JSONObject result = null;
		try {
			if (isNotBlank(str) && str.contains("{") && str.contains(":") && str.contains("}")) {
				result =  JSONObject.fromObject(str.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 判断一个对象是否为JSON数组
	 * 
	 * @param object
	 * @return 是则返回JSON数组，不是则返回null
	 */
	public static JSONArray isJSONArray(Object object) {
		JSONArray result = null;
		if (isNotBlank(object) && object instanceof JSONArray) {
			result = new JSONArray();
			for (Object json : (JSONArray) object) {
				if (json != null && json instanceof JSONObject) {
					result.add(json);
					continue;
				} else {
					result.add(JSONObject.fromObject(json));
				}
			}
		}
		return result;
	}

	/**
	 * 对字符串进行BASE64编码
	 * 
	 * @param str 目的字符串
	 * @param bf  [true|false]：true:去掉结尾补充的'='；false:不做处理；
	 * @return
	 */
	public static String getBASE64(String str, boolean... bf) {
		String result = null;
		if (StringUtils.isNotNullOrEmpty(str)) {
			result = Base64.encodeBase64String(str.getBytes());
			// 判断是否要去掉结尾的 '='
			if (isBlank(bf) && bf[0]) {
				result = result.replaceAll("=", "");
			}
		}
		return result;
	}

	/**
	 * 解码BASE64编码的字符串
	 * 
	 * @param base64Str
	 * @return
	 */
	public static String getStrByBASE64(String base64Str) {
		String result = "";
		try {
			if (isNotBlank(base64Str)) {
				byte[] byteArray = Base64.decodeBase64(base64Str);
				result = new String(byteArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 把Map转换成get请求参数类型,如 {"name"=20,"age"=30} 转换后变成 name=20&age=30
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToGetParams(Map<? extends Object, ? extends Object> map) {
		String result = "";
		if (map == null || map.size() == 0) {
			return result;
		} else {
			Set<? extends Object> keys = map.keySet();
			for (Object key : keys) {
				result += ((String) key + "=" + (String) map.get(key) + "&");
			}
		}
		return isBlank(result) ? result : result.substring(0, result.length() - 1);
	}

	/**
	 * 把一串参数字符串,转换成Map 如"?a=3&b=4" 转换为Map{a=3,b=4}
	 * 
	 * @param args
	 * @return
	 */
	public static Map<String, ? extends Object> getToMap(String args) {
		Map<String, Object> result = null;
		if (isNotBlank(args)) {
			args = args.trim();
			// 如果是?开头,把?去掉
			if (args.startsWith("?")) {
				args = args.substring(1, args.length());
			}
			String[] argsArray = args.split("&");

			result = new HashMap<String, Object>();
			for (String ag : argsArray) {
				if (isNotBlank(ag) && ag.indexOf("=") > 0) {
					String[] keyValue = ag.split("=");
					// 如果value或者key值里包含 "="号,以第一个"="号为主 ,如 name=0=3 转换后,{"name":"0=3"},
					// 如果不满足需求,请勿修改,自行解决.
					String key = keyValue[0];
					String value = "";
					for (int i = 1; i < keyValue.length; i++) {
						value += keyValue[i] + "=";
					}
					value = value.length() > 0 ? value.substring(0, value.length() - 1) : value;
					result.put(key, value);
				}
			}
		}
		return result;
	}

	/**
	 * 转换成Unicode
	 * 
	 * @param str
	 * @return
	 */
	public static String toUnicode(String str) {
		String as[] = new String[str.length()];
		String s1 = "";
		for (int i = 0; i < str.length(); i++) {
			int v = str.charAt(i);
			if (v >= 19968 && v <= 171941) {
				as[i] = Integer.toHexString(str.charAt(i) & 0xffff);
				s1 = s1 + "\\u" + as[i];
			} else {
				s1 = s1 + str.charAt(i);
			}
		}
		return s1;
	}

	/**
	 * 合并数据
	 * 
	 * @param v
	 * @return
	 */
	public static String merge(Object... v) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < v.length; i++) {
			sb.append(v[i]);
		}
		return sb.toString();
	}

	/**
	 * 字符串转urlcode
	 * 
	 * @param value
	 * @return
	 */
	public static String strToUrlcode(String value) {
		String result = null;
		try {
			result = URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LoggerUtils.error(StringUtils.class, "字符串转换为URLCode失败,value:" + value, e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * urlcode转字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String urlcodeToStr(String value) {
		String result = null;
		try {
			result = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LoggerUtils.error(StringUtils.class, "URLCode转换为字符串失败;value:" + value, e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断字符串是否包含汉字
	 * 
	 * @param txt
	 * @return
	 */
	public static Boolean containsCN(String txt) {
		boolean flag = false;
		if (isNotBlank(txt)) {
			for (int i = 0; i < txt.length(); i++) {
				String str = txt.substring(i, i + 1);
				boolean result = Pattern.matches("[\u4E00-\u9FA5]", str);
				if (result) {
					flag = result;
				}
			}
		}
		return flag;
	}

	/**
	 * 去掉HTML代码
	 * 
	 * @param news
	 * @return
	 */
	public static String removeHtml(String news) {
		String s = news.replaceAll("amp;", "").replaceAll("<", "<").replaceAll(">", ">");

		Pattern pattern = Pattern.compile("<(span)?\\sstyle.*?style>|(span)?\\sstyle=.*?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(s);
		String str = matcher.replaceAll("");

		Pattern pattern2 = Pattern.compile("(<[^>]+>)", Pattern.DOTALL);
		Matcher matcher2 = pattern2.matcher(str);
		String strhttp = matcher2.replaceAll(" ");

		String regEx = "(((http|https|ftp)(\\s)*((\\:)|：))(\\s)*(//|//)(\\s)*)?"
				+ "([\\sa-zA-Z0-9(\\.|．)(\\s)*\\-]+((\\:)|(:)[\\sa-zA-Z0-9(\\.|．)&%\\$\\-]+)*@(\\s)*)?" + "("
				+ "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])" + "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])"
				+ "|([\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*)*[\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*[\\sa-zA-Z]*" + ")" + "((\\s)*(\\:)|(：)(\\s)*[0-9]+)?"
				+ "(/(\\s)*[^/][\\sa-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*";
		Pattern p1 = Pattern.compile(regEx, Pattern.DOTALL);
		Matcher matchhttp = p1.matcher(strhttp);
		String strnew = matchhttp.replaceAll("").replaceAll("(if[\\s]*\\(|else|elseif[\\s]*\\().*?;", " ");

		Pattern patterncomma = Pattern.compile("(&[^;]+;)", Pattern.DOTALL);
		Matcher matchercomma = patterncomma.matcher(strnew);
		String strout = matchercomma.replaceAll(" ");
		String answer = strout.replaceAll("[\\pP‘’“”]", " ").replaceAll("\r", " ").replaceAll("\n", " ").replaceAll("\\s", " ").replaceAll("　", "");

		return answer;
	}

	/**
	 * 把数组的空数据去掉
	 * 
	 * @param array
	 * @return
	 */
	public static List<String> filterEmptyElement(String[] array) {
		List<String> list = new ArrayList<String>();
		for (String string : array) {
			if (StringUtils.isNotBlank(string)) {
				list.add(string);
			}
		}
		return list;
	}

	/**
	 * 把数组的空数据去掉
	 * 
	 * @param array
	 * @return
	 */
	public static List<String> filterEmptyElement(List<String> array) {
		List<String> list = new ArrayList<String>();

		for (String string : array) {
			if (StringUtils.isNotBlank(string)) {
				list.add(string);
			}
		}
		return list;
	}

	/**
	 * 把数组转换成set
	 * 
	 * @param array
	 * @return
	 */
	public static Set<?> array2Set(Object[] array) {
		Set<Object> set = new TreeSet<Object>();
		for (Object id : array) {
			if (null != id) {
				set.add(id);
			}
		}
		return set;
	}

	/**
	 * serializable toString
	 * 
	 * @param serializable
	 * @return
	 */
	public static String toString(Serializable serializable) {
		String result = null;
		try {
			if (null != serializable) {
				result = (String) serializable;
			}
		} catch (Exception e) {
			return serializable.toString();
		}
		return result;
	}

	/**
	 * 去除首尾指定字符
	 * @param str   字符串
	 * @param element   指定字符
	 * @return
	 */
	public static String trimFirstAndLastChar(String str, String element){
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do{
			int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
			int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
			str = str.substring(beginIndex, endIndex);
			beginIndexFlag = (str.indexOf(element) == 0);
			endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
		} while (beginIndexFlag || endIndexFlag);
		return str;
	}

}
