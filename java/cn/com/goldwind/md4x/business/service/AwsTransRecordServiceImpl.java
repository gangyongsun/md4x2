/**   
* @Title: F7scadaMd4XDbImpl.java 
* @Package cn.com.goldwind.md4x.business.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 26, 2020 1:16:13 PM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.goldwind.md4x.business.bo.DataintegrityVO;
import cn.com.goldwind.md4x.business.dao.datacenter.AwsTransRecordDao;
import cn.com.goldwind.md4x.business.dao.datacenter.FObjectdataS3DbDao;
import cn.com.goldwind.md4x.business.entity.datacenter.AwsTransRecord;
import cn.com.goldwind.md4x.business.entity.datacenter.FObjectdataS3Db;
import cn.com.goldwind.md4x.mybatis.Condition;
import cn.com.goldwind.md4x.mybatis.ResultMapData;
import cn.com.goldwind.md4x.mybatis.ResultMapDataDao;

/**
 * @ClassName: F7scadaMd4XDbImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wangguiyu
 * @date Apr 26, 2020 1:16:13 PM
 *
 */
@Service
public class AwsTransRecordServiceImpl implements AwsTransRecordService {

	@Autowired
	AwsTransRecordDao awsTransRecordDao;

	@Autowired
	FObjectdataS3DbDao fObjectdataS3DbDao;
	
	@Autowired
	ResultMapDataDao resultMapDataDao;

	@Override
	public Map<String,Object> getDataIntegrity(String dataType, Date startDate, Date endDate, String wfid, String wtids) {
		String ret = "";//数据完整度
		List<AwsTransRecord> fileCount = new ArrayList<AwsTransRecord>();//按月统计7s文件数
		Map<String,List<DataintegrityVO>> dataintegrity = new HashMap<String,List<DataintegrityVO>>();
		
		Date startDate0 = new Date();
		Date endDate0 = new Date();
		// --查询项目并网时间，如果并网时间为空，查询F7scadaMd4XDb中查询data_date最小值
		if (startDate == null) {
			//Condition c = Condition.newInstance();
			//c.addCondition("systemdefinedfarmid", wfid);
			// 查询并网时间，没有并网时间字段
			// List<HuinengWindFarmInfo> list = windFarmInfoDao.searchListByCondition(c);
			// if(list != null && list.size()>0) {
			// list.get(0).get 并网时间
			// }

			// 查询F7scadaMd4XDb中查询data_date最小值 ******
			startDate0 = new Date(2015, 9, 14);

		} else {
			startDate0 = startDate;
		}
		// 如果为空，获取当前时间
		if (endDate == null) {
			endDate0 = new Date();
		} else {
			endDate0 = endDate;
		}
		int days = 0;
        //7秒文件的数据完整度
		if ("7s".equals(dataType)) {
			days = differentDays(startDate0, endDate0);// 天数
			Condition c = Condition.newInstance();
			int wtCount = 0;
			if (StringUtils.isBlank(wtids)) {//指定一个风场(全部风机)
				// 查询风机总数，通过风场id查询风机表 huineng_wind_fan_info 中字段scada_deviceId字段前6位（模糊匹配）
				//wtCount = windFanInfoDao.getCountByCondition(Condition.newInstance().addCondition("scada_deviceId", wfid, Condition.LIKE));
				ResultMapData resultMapData = new ResultMapData();
				resultMapData.put("wfid", wfid);
				resultMapData = resultMapDataDao.selectOne("groupAllWindTurbines", resultMapData);
				if(resultMapData != null) {
					Double d = Double.parseDouble(String.valueOf(resultMapData.get("windTurbineTotals")));
					wtCount = new Double(d).intValue();
				}
				
				c.addMapCondition("wfid", wfid);
				c.addMapCondition("startDate", getDateString(startDate0)).addMapCondition("endDate", getDateString(endDate0));
				fileCount = awsTransRecordDao.selectList("getAllByMonth", c);
						
				List<DataintegrityVO> list = new ArrayList<DataintegrityVO>();
				for(AwsTransRecord f:fileCount) {
					DataintegrityVO dvo = new DataintegrityVO();
					String dataMonth = f.getMonth();
					dvo.setDataMonth(dataMonth);
					int b = f.getCount();//一个风场当月所有风机的 实际文件数
					int dayMonth = getDaysByYYYYMM(dataMonth);//理论上每台风机一天一个7s文件，当月有多少天，就多少个7s文件
					int a = dayMonth * wtCount;//一个风场当月所有风机的 理论文件数
					
					DecimalFormat df = new DecimalFormat("0.00");
					String rate = df.format((float) b / a * 100);
					dvo.setRate(rate);
					list.add(dvo);
				}
				dataintegrity.put("all", list);
				
			} else {//指定一个风场的部分风机
				String wtidss[] = wtids.split(",");
				wtCount = wtidss.length;
				c.addCondition("wtid", wtids, Condition.IN);
				c.addMapCondition("startDate", getDateString(startDate0)).addMapCondition("endDate", getDateString(endDate0));
				c.addMapCondition("wfid", wfid);
				c.addMapCondition("wtids", "("+wtids+")");
				fileCount = awsTransRecordDao.selectList("getByMonth", c);
				
				for(AwsTransRecord f:fileCount) {
					String wtid = f.getWtid();
					String dataMonth = f.getMonth();
					int b = f.getCount(); //一个风机的当月实际文件数
					int a = getDaysByYYYYMM(dataMonth);//理论上每台风机一天一个7s文件，当月有多少天，就多少个7s文件
					DecimalFormat df = new DecimalFormat("0.00");
					String rate = df.format((float) b / a * 100);
					DataintegrityVO dvo = new DataintegrityVO();
					dvo.setDataMonth(dataMonth);
					dvo.setRate(rate);
					
					List<DataintegrityVO> listValue = dataintegrity.get(wtid);
					if(listValue != null) {
						listValue.add(dvo);
					}else {
						List<DataintegrityVO> list = new ArrayList<DataintegrityVO>();
						list.add(dvo);
						dataintegrity.put(wtid, list);
					}
				}
				
							
			}
			//a.理论上7s文件总数(指定时段、指定风场、指定风机)风机数*天数,每个风机一天一个7s文件
			int a = days * wtCount;

			//b.实际的7秒数据文件总数(指定时段、指定风场、指定风机),查库得到
			c.addCondition("day", getDateString(startDate0), Condition.GT_EQ);
			c.addCondition("day", getDateString(endDate0), Condition.LT);
			c.addCondition("wfid", wfid);
			int b = awsTransRecordDao.getCountByCondition(c);
			 
			//c.符合条件的数据完整度，即b/a
			DecimalFormat df = new DecimalFormat("0.00");
			if(a == 0)
				ret = "0.00%";
			else
				ret = df.format((float) b / a * 100) + "%";
           
		} else {//其他文件类型 dataType=B、F、O、W
			String fileType = null;
			switch(dataType) {
			case "B":
				fileType = "PlcBFile";
				break;
			case "F":
				fileType = "PlcFFile";
				break;
			case "O":
				fileType = "PlcOFile";
				break;
			case "W":
				fileType = "PlcWFile";
				break;
			default:
				fileType = "PlcBFile";
			}
			List<FObjectdataS3Db> fObjectdataS3DbList = new ArrayList<FObjectdataS3Db>();
			//一个风场某一类型文件总数，按月分组
			if (StringUtils.isBlank(wtids)) {// 指定一个风场(全部风机)
				//查询指定风场、时段、文件类型的个数
				int ifileCount = fObjectdataS3DbDao.getCountByCondition(
						Condition.newInstance().addCondition("wfid", wfid).addCondition("type", fileType)
								.addCondition("data_date", getDateString(startDate0), Condition.GT_EQ).addCondition("data_date", getDateString(endDate0),Condition.LT_EQ));
                ret = ifileCount+"";
                Condition c = Condition.newInstance();
                c.addMapCondition("wfid", wfid);
				c.addMapCondition("startDate", getDateString(startDate0)).addMapCondition("endDate", getDateString(endDate0)).addMapCondition("dataType", fileType);
				fObjectdataS3DbList = fObjectdataS3DbDao.selectList("getAllByMonth", c);
				
				List<DataintegrityVO> list = new ArrayList<DataintegrityVO>();
				for(FObjectdataS3Db f:fObjectdataS3DbList) {
					DataintegrityVO dvo = new DataintegrityVO();
					dvo.setDataMonth(f.getDataMonth());
					dvo.setRate(f.getCount()+"");//文件数
					list.add(dvo);
				}
				dataintegrity.put("all", list);
                
			}
			//一个风场指定风机某一类型文件总数，按月分组
			else {//指定一个风场的部分风机
				//查询指定风场、时段、文件类型的个数
				int ifileCount = fObjectdataS3DbDao.getCountByCondition(
						Condition.newInstance().addCondition("wfid", wfid).addCondition("type", fileType)
								.addCondition("data_date", getDateString(startDate0), Condition.GT_EQ)
								.addCondition("data_date", getDateString(endDate0), Condition.LT)
						        .addCondition("wtid", wtids,Condition.IN));
                ret = ifileCount+"";
                
				Condition c = Condition.newInstance();
				c.addCondition("wtid", wtids, Condition.IN);
				c.addMapCondition("startDate", getDateString(startDate0)).addMapCondition("endDate", getDateString(endDate0));
				c.addMapCondition("wfid", wfid);
				c.addMapCondition("wtids", "("+wtids+")");
				c.addMapCondition("dataType", fileType);
				fObjectdataS3DbList = fObjectdataS3DbDao.selectList("getByMonth", c);
				
				for(FObjectdataS3Db f:fObjectdataS3DbList) {
					String wtid = f.getWtid();
					DataintegrityVO dvo = new DataintegrityVO();
					dvo.setDataMonth(f.getDataMonth());
					dvo.setRate(f.getCount()+"");
					List<DataintegrityVO> listValue = dataintegrity.get(wtid);
					if(listValue != null) {
						listValue.add(dvo);
					}else {
						List<DataintegrityVO> list = new ArrayList<DataintegrityVO>();
						list.add(dvo);
						dataintegrity.put(wtid, list);
					}
				}			
			}
		}

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ret", ret);
		map.put("dataintegrity", dataintegrity);
		return map;
	}

	
	//前1个月最后一天
	private static Date getPreMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		//月份减1
		calendar.add(Calendar.MONTH, -1);
		//计算当月最大天数
		int maxCurrentMonthDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设为月末最后一天
		calendar.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
		//SimpleDateFormat s = new SimpleDateFormat("YYYY-MM-dd");
		//String date = s.format(calendar.getTime());
		//return date;
		return calendar.getTime();
	}

	//前12个月第一天
	private static Date getPre12MonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		//月份减1
		calendar.add(Calendar.MONTH, 0);
		//设为月末最后一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
		
		
	/**
	 * startDate
	 * endDate
	 * 计算所有风场近12个月内7s数据完整度,如果不指定起始时间，则计算上个月向前推12个月的数据完整度
	 */
	public Map<String,Double> calculateDataIntegrity(Date startDate,Date endDate) {
		Date startDate0 = getPre12MonthFirstDay();
		Date endDate0 = getPreMonthLastDay();
		
		if(startDate != null) {
			startDate0 = startDate;
		}
		
		if(endDate != null) {
			endDate0 = endDate;
		}
		
		int days = differentDays(startDate0, endDate0);// 天数
		
		//风场id与风机数 map
		List<ResultMapData> wtcounts = resultMapDataDao.selectList("getWtCount", Condition.newInstance());
		Map<String,Double> wtcountMap = new HashMap<String,Double>();
		for(ResultMapData rmd:wtcounts) {
			String wfid = rmd.getString("wfid");
			if(StringUtils.isNotEmpty(wfid)) {
				wtcountMap.put(wfid, Double.parseDouble(rmd.get("wtcount").toString()));
			}
		}
		
		//风场id与7s文件数 map
		List<ResultMapData> filecounts = resultMapDataDao.selectList("get7sFileCount", Condition.newInstance().addMapCondition("startDate", getDateString(startDate0)).addMapCondition("endDate", getDateString(endDate0)));
		Map<String,Double> dataIntegrityMap = new HashMap<String,Double>();//风场7s数据完整度
		for(ResultMapData rmd : filecounts) {
			String wfid = rmd.getString("wfid");
			if(StringUtils.isNotEmpty(wfid)) {
				Double filecount = Double.parseDouble(rmd.get("filecount").toString());//实际7s文件数
				Double wtcount = wtcountMap.get(wfid);//风场风机数
				if(wtcount == null) {
					wtcount = 0.0d;
				}
				DecimalFormat df = new DecimalFormat("0.00");
				//理论上7s文件总数(指定时段、指定风场、指定风机)风机数*天数,每个风机一天一个7s文件
				double a = days * wtcount;
				
				double ret = 0d;
				if(a == 0)
					ret = 0;
				else
					ret = Double.parseDouble(df.format(filecount / a * 100));
				dataIntegrityMap.put(wfid, ret);
			}
		}
		
		return dataIntegrityMap;
	}

	
	
	//******************************************************************
	private static String getDateString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		return dateString;
	}
	
	//指定时段的天数差值( n<= date1 && n>date2)
	private static int differentDays(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) // 同一年
		{
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
				{
					timeDistance += 366;
				} else // 不是闰年
				{
					timeDistance += 365;
				}
			}
			// System.out.println("判断day2 - day1 : " + (timeDistance + (day2 - day1)));
			return timeDistance + (day2 - day1);
		} else // 不同年
		{
			// System.out.println("判断day2 - day1 : " + (day2 - day1));
			return day2 - day1;
		}
	}

	// 通过 YYYYMM 年月 获取天数，如参数为202002时返回29
	private static int getDaysByYYYYMM(String yyyymm) {
		String dyear = yyyymm.substring(0, 4);
		String dmouth = yyyymm.substring(4, 6);
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
		Calendar rightNow = Calendar.getInstance();
		try {
			rightNow.setTime(simpleDate.parse(dyear + "/" + dmouth));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);// 根据年月 获取月份天数
		return days;
	}

	public static void main(String[] args) {
	}

}
