package cn.com.goldwind.md4x.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期公共类
 * <p>
 * 日期格式有以下格式
 * <p>
 * "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
 * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
 * 
 * @author yonggang
 *
 */
public class DateUtil extends DateUtils {
	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
			"yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 格式化当前时间，返回Strings
	 * 
	 * @param pattern 格式
	 * @return 格式化后的时间字符串
	 */
	public static String getCurrentDateStr(String pattern) {
		return date2StringPattern(new Date(),pattern);
	}

	/**
	 * String类型日期格式化为Date类型
	 * 
	 * @param dateStr 日期
	 * @param pattern  格式
	 * @return
	 */
	public static Date stringDate2Date(String dateStr, String pattern) {
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Date类型日期格式化为String类型日期
	 * 
	 * @param date 日期对象
	 * @return String格式日期
	 */
	public static String date2StringPattern(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * String类型日期格式转换
	 * 
	 * @param dateStr   字符串格式日期
	 * @param oldPattern 原始类型
	 * @param newPattern 目的类型
	 * @return
	 */
	public static String stringDatePatternConvert(String dateStr, String oldPattern, String newPattern) {
		String newDate = "";
		if(StringUtils.isNotBlank(dateStr,oldPattern,newPattern)){
			newDate = date2StringPattern(stringDate2Date(dateStr, oldPattern), newPattern);
		}
		return newDate;
	}

	/**
	 * 日期比较
	 * 
	 * @param dateA
	 * @param dateB
	 * @return 如果dateA>=dateB 返回true；否则返回false
	 */
	public static boolean compareDate(Date dateA, Date dateB) {
		boolean flag = false;
		if(StringUtils.isNotBlank(dateA,dateB)){
			flag = dateA.getTime() >= dateB.getTime();
		}
		return flag;
	}

	/**
	 * 日期比较,
	 * 
	 * @param str_dateA 字符串格式日期
	 * @param str_dateB 字符串格式日期
	 * @param pattern  格式
	 * @return 如果dateA>=dateB 返回true；否则返回false
	 */
	public static boolean compareDate(String str_dateA, String str_dateB, String pattern) {
		Date formated_dateA = stringDate2Date(str_dateA, pattern);
		Date formated_dateB = stringDate2Date(str_dateB, pattern);
		return compareDate(formated_dateA, formated_dateB);
	}

	/**
	 * 获取时间间隔,单位：年
	 * 
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 相差年数
	 */
	public static int getDiffYear(String startTime, String endTime) {
		int years = 0;
		try {
			DateFormat yMd_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
			years = (int) (((yMd_FORMAT.parse(endTime).getTime() - yMd_FORMAT.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return years;
	}

	/**
	 * 时间相减得到天数
	 * 
	 * @param str_startDate 开始时间
	 * @param str_endDate   结束时间
	 * @return long 相隔天数
	 */
	public static long getDaySub(String str_startDate, String str_endDate) {
		String pattern = "yyyy-MM-dd";
		return (stringDate2Date(str_endDate, pattern).getTime() - stringDate2Date(str_startDate, pattern).getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 得到n天之后的日期
	 * 
	 * @param num 天数
	 * 
	 * @param pattern 格式
	 * @return
	 */
	public static String getIncreaseDateStr(int num, String pattern) {
		Calendar canlendar = Calendar.getInstance();
		canlendar.add(Calendar.DATE, num);
		return new SimpleDateFormat(pattern).format(canlendar.getTime());
	}

	/**
	 * 得到n天之前的日期
	 * 
	 * @param days    天数
	 * @param pattern 格式
	 * @return
	 */
	public static String getDecreaseDateStr(int days, String pattern) {
		return getIncreaseDateStr(-days, pattern);
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param num
	 * @return
	 */
	public static String whichDayAfterNum(int num) {
		Calendar canlendar = Calendar.getInstance();
		canlendar.add(Calendar.DATE, num);
		Date date = canlendar.getTime();
		return new SimpleDateFormat("E").format(date);
	}

	/**
	 * 秒转化成天时分秒
	 * 
	 * @param seconds
	 * @return
	 */
	public static String seconds2StrDate(long seconds) {
		int mi = 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = seconds / dd;
		long hour = (seconds - day * dd) / hh;
		long minute = (seconds - day * dd - hour * hh) / mi;
		long second = (seconds - day * dd - hour * hh - minute * mi);

		String strDay = day < 10 ? "0" + day : "" + day; // 天
		String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
		String strSecond = second < 10 ? "0" + second : "" + second;// 秒

		String basicResult = strHour + "小时" + strMinute + "分钟" + strSecond + "秒";
		String result = !"00".equals(strDay) ? strDay + "天" + basicResult : basicResult;
		return result;
	}

	/**
	 * 获取当前时间对应的Timestamp
	 * 
	 * @return
	 */
	public static Timestamp getCurrentDateInTimestamp() {
		return Timestamp.valueOf(getCurrentDateStr("yyyy-MM-dd HH:mm:ss"));
	}

	public static String countdate(String str_date) {
		Date currentDate = getCurrentDateInTimestamp();
		int currentYear = Integer.parseInt(currentDate.toString().substring(0, 4));
		try {
			String ret = "";
			long create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str_date).getTime();
			Calendar calendar = Calendar.getInstance();
			long ms = 1000 * (calendar.get(11) * 3600 + calendar.get(12) * 60 + calendar.get(13));
			long ms_now = calendar.getTimeInMillis();
			int year = Integer.parseInt(str_date.substring(0, 4));
			int hour = Integer.parseInt(str_date.substring(11, str_date.length() - 6));
			if (ms_now - create < ms) {
				ret = str_date.substring(11, str_date.length() - 3);
				if ((hour >= 5) && (hour < 9)) {
					ret = "早上 " + str_date.substring(11, str_date.length() - 3);
				}
				if ((hour >= 9) && (hour < 12)) {
					ret = "上午 " + str_date.substring(11, str_date.length() - 3);
				}
				if ((hour >= 12) && (hour < 18)) {
					ret = "下午 " + str_date.substring(11, str_date.length() - 3);
				}
			} else if (ms_now - create < ms + 86400000L) {
				ret = "昨天";
			} else if (currentYear == year) {
				ret = str_date.substring(5, 7) + "月" + str_date.substring(8, 10) + "日";
			} else {
				ret = str_date.substring(0, 4) + "年" + str_date.substring(5, 7) + "月" + str_date.substring(8, 10) + "日";
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断日期前后
	 * 
	 * @param dateA
	 * @param dateB
	 * @return
	 * @throws ParseException
	 */
	public static boolean is_DateA_after_DateB(String dateA, String dateB) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		Date date1 = simpleDateFormat.parse(dateA);
		Date date2 = simpleDateFormat.parse(dateB);
		return date1.after(date2);
	}

	/**
	 * 时间转换为毫秒
	 * 
	 * @param date 时间
	 * @return
	 */
	public static long date2millionSeconds(Date date) {
		return date.getTime();
	}

	/**
	 * 获取过去了多少天
	 * 
	 * @param date 对比日期
	 * @return
	 */
	public static long pastDays(Date date) {
		return ((new Date().getTime() - date.getTime()) / 86400000L);
	}

	/**
	 * 获取过去了多少小时
	 * 
	 * @param date 起始时间点
	 * @return 过去了多少小时
	 */
	public static long pastHours(long milliseconds) {
		return ((new Date().getTime() - milliseconds) / 3600000L);
	}

	/**
	 * 获取过去了多少小时
	 * 
	 * @param date 起始时间点
	 * @return 过去了多少小时
	 */
	public static long pastHours(Date date) {
		return ((new Date().getTime() - date.getTime()) / 3600000L);
	}

	/**
	 * 获取过去了多少分钟
	 * 
	 * @param date 起始时间点
	 * @return 过去了多少分钟
	 */
	public static long pastMinutes(Date date) {
		return ((new Date().getTime() - date.getTime()) / 60000L);
	}

	/**
	 * 格式化毫秒格式日期
	 * 
	 * @param milliseconds 毫秒格式日期
	 * @return
	 */
	public static String formatDateTime(long milliseconds) {
		long day = milliseconds / 86400000L;
		long hour = milliseconds / 3600000L - (day * 24L);
		long min = milliseconds / 60000L - (day * 24L * 60L) - (hour * 60L);
		long s = milliseconds / 1000L - (day * 24L * 60L * 60L) - (hour * 60L * 60L) - (min * 60L);
		long sss = milliseconds - (day * 24L * 60L * 60L * 1000L) - (hour * 60L * 60L * 1000L) - (min * 60L * 1000L) - (s * 1000L);
		return ((day > 0L) ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 增加n个月
	 * 
	 * @param str_date   日期
	 * @param mountCount 月数
	 * @return
	 */
	public static String add_months(String str_date, int mountCount) throws ParseException {
		String pattern = "yyyy-MM";
		Date date = stringDate2Date(str_date, pattern);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, mountCount);

		return date2StringPattern(calendar.getTime(), pattern);
	}

	/**
	 * 增加天数
	 * 
	 * @param date     指定日期
	 * @param dayCount 指定天数
	 * @return
	 */
	public static Date add_days(Date date, int dayCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, dayCount);
		return calendar.getTime();
	}

	public static Date beforeDay(int beforday) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - beforday);
		return calendar.getTime();
	}

	public static double getDistanceOfTwoDate(Date before, Date after) {
		return ((after.getTime() - before.getTime()) / 86400000L);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date    日期
	 * @param pattern 格式
	 * @return
	 */
	public static String formatDate(Date date, Object[] pattern) {
		String formatDate = null;
		if ((pattern != null) && (pattern.length > 0))
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	public static String formatDateTime(Date date) {
		return formatDate(date, new Object[] { "yyyy-MM-dd HH:mm:ss" });
	}

	public static String getTimeHMS() {
		return formatDate(new Date(), new Object[] { "HH:mm:ss" });
	}

	public static String getDateTime() {
		return formatDate(new Date(), new Object[] { "yyyy-MM-dd HH:mm:ss" });
	}

	public static String getMonth() {
		return formatDate(new Date(), new Object[] { "MM" });
	}

	public static String getDayDD() {
		return formatDate(new Date(), new Object[] { "dd" });
	}

	public static String getWeek() {
		return formatDate(new Date(), new Object[] { "E" });
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		Date date = null;
		if (str != null) {
			try {
				date = parseDate(str.toString(), parsePatterns);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return date;
	}
}
