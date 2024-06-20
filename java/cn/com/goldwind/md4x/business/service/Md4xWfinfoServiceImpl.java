/**   
* @Title: TWfinfoServiceImpl.java 
* @Package cn.com.goldwind.md4x.business.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 15, 2020 8:33:39 AM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.goldwind.md4x.business.bo.Md4XWfinfoVo;
import cn.com.goldwind.md4x.business.bo.Md4xWfinfoSummaryVO;
import cn.com.goldwind.md4x.business.bo.WindFarmInfo;
import cn.com.goldwind.md4x.business.bo.WindFarmInfoVO;
import cn.com.goldwind.md4x.business.bo.WindFarmOwnerInfo;
import cn.com.goldwind.md4x.business.bo.WindFarmTypeInfo;
import cn.com.goldwind.md4x.business.dao.datacenter.Md4xWfinfoDao;
import cn.com.goldwind.md4x.business.entity.datacenter.Md4xWfinfo;
import cn.com.goldwind.md4x.mybatis.Condition;
import cn.com.goldwind.md4x.mybatis.IGenericPage;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.mybatis.ResultMapData;
import cn.com.goldwind.md4x.mybatis.ResultMapDataDao;

/**
 * @ClassName: TWfinfoServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wangguiyu
 * @date Apr 15, 2020 8:33:39 AM
 *
 */

@Service
public class Md4xWfinfoServiceImpl implements Md4xWfinfoService {
	
	@Autowired
	ResultMapDataDao resultMapDataDao;
	
	@Autowired
	Md4xWfinfoDao md4xWfinfoDao;
	
	@Autowired
	AwsTransRecordService awsTransRecordService;

	@Override
	public Page<WindFarmInfoVO> getWindFarminfo(Integer pageNo, Integer pageSize, String searchKey,String searchValue,String province,String sortFlag) {
		// 1. 查询条件
		Condition c = Condition.newInstance();
		if (searchKey != null) {
			if (searchKey.equals("wfid")) {
				c.addMapCondition("wfid", searchKey);
			}
			else if (searchKey.equals("wfname")) {//风场名
				c.addMapCondition("wfname", searchKey);
			}
			else if (searchKey.equals("owner")) {//业主
				c.addMapCondition("owner", searchKey);
			}
			else if (searchKey.equals("type")) {//机型
				c.addMapCondition("type", searchKey);
			}
		}
		if (!StringUtils.isEmpty(searchValue)) {
			c.addMapCondition("searchValue", searchValue);
		}
        c.addMapCondition("province", province);
        
        // 2.不考虑分页的条件查询，md4xWfinfos 风场有重复，同一风场，每种几型有一条记录
    	c.setLimit(null);
		c.setStart(null);
		List<Md4xWfinfo> md4xWfinfos = md4xWfinfoDao.selectList("searchList",c);
		
		// 3.所有风场数据完整度 map<风场id，数据完整度值>
		//当前时间YYYY-MM-DD 作为查询截止时间
		LocalDate endDateL = LocalDate.now();
		Date endDate = Date.from(endDateL.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
	    //查询起始时间，向前推两年
	    LocalDate startDateL = endDateL.minusYears(2);
	    Date startDate = Date.from(startDateL.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
	      
		Map<String, Double> dataIntegrity = awsTransRecordService.calculateDataIntegrity(startDate,endDate);
        
		 List<Md4xWfinfo> gWFinalMD4xWfinfo = null;//每页数据
	     int totalCount = 0;//总记录数
	     Integer start = 0;//起始记录序号
	     //String sortFlag = "capacity";//capacity or  dataIntegrity
	     
		// 5.按照装机容量从大到小的排序，一页数据
	    if("wfcapacity".equalsIgnoreCase(sortFlag)) {
	    	if (pageNo == null || pageSize == null) {
				pageSize = md4xWfinfoDao.getCountByCondition("getCountByConditionWithPage", c);//按风场名分组统计，没有重复
				if(pageSize == 0)
					return null;
			} else {
				if (pageNo <= 0)
					start = 0;
				else
					start = (pageNo - 1) * pageSize;
			}
			c.setLimit(pageSize).setStart(start);
			IGenericPage<Md4xWfinfo> genericPage = md4xWfinfoDao.selectPageBy("searchPageBy", c);// 3.有分页条件的查询
			gWFinalMD4xWfinfo = genericPage.getThisPageElements();
			totalCount = genericPage.getTotalCount();
	    }else if("dataIntegrity".equalsIgnoreCase(sortFlag)) {
	    	Set<String> wfnameSet = new HashSet<String>();//对 md4xWfinfos集合 按照wf_name分组，去重,保存到新的集合 windFarms
	    	List<Md4xWfinfo> windFarms = new ArrayList<Md4xWfinfo>();
	    	//6.对符合条件的数据 按照数据完整度从大到小排序，假分页
	    	for(Md4xWfinfo wfinfo:md4xWfinfos) {
	    		if(!wfnameSet.contains(wfinfo.getWfName())) {
	    			Double dataIntegrityValue = dataIntegrity.get(wfinfo.getWfid());
					if(dataIntegrityValue == null)
						dataIntegrityValue = 0.00d;
		    		wfinfo.setDataIntegrity(dataIntegrityValue);
		    		wfnameSet.add(wfinfo.getWfName());
		    		windFarms.add(wfinfo);
	    		}
	    	}
	    	
	    	windFarms.sort(Comparator.comparing(Md4xWfinfo::getDataIntegrity).reversed());//按照数据完整度倒序排序
	    	totalCount = windFarms.size();//符合条件的总数
	    	
	    	if (pageNo == null || pageSize == null) {
				pageSize = totalCount;
				if(pageSize == 0)
					return null;
			} else {
				if (pageNo <= 0)
					start = 0;
				else
					start = (pageNo - 1) * pageSize;
			}
	    	int toIndex = pageNo*pageSize;
	    	if(toIndex>totalCount) {
	    		toIndex = totalCount;
	    	}
	    	gWFinalMD4xWfinfo = windFarms.subList(start, toIndex);//1页数据
	    	
	    }
		
			
		Condition c1 = Condition.newInstance();
		c1.addSort("wf_name", Condition.SORT_DESC);
		List<Md4xWfinfo> lists = md4xWfinfoDao.searchListByCondition(c1);// 4. windFarmMap 所有风场的一些统计信息：  
	    Map<String,Md4XWfinfoVo> windFarmMap = new HashMap<String,Md4XWfinfoVo>();
		for(Md4xWfinfo wfinfo : lists) {
			String wfName = wfinfo.getWfName();
			String model = wfinfo.getModel();
			String wtype = wfinfo.getWtype();
			int number = Integer.parseInt(wfinfo.getNumber());
			double scapacity = wfinfo.getInstalledCapacity()!=null? wfinfo.getInstalledCapacity().doubleValue():0.0d;
			
			List<String> modes = new ArrayList<String>();
			List<String> mode_types = new ArrayList<String>();
			Set<String> wtypes = new HashSet<String>();
			Integer windTurbinCount = 0;
			double fcapacity = 0.0f;
			Md4XWfinfoVo md4XWfinfoVo = new Md4XWfinfoVo();
			if(windFarmMap.get(wfName) != null ) {
				md4XWfinfoVo = windFarmMap.get(wfName);
				modes = md4XWfinfoVo.getModels();
				mode_types = md4XWfinfoVo.getModelTypes();	
				windTurbinCount = md4XWfinfoVo.getWindTurbinCount();
				wtypes = md4XWfinfoVo.getWtype();
				fcapacity = md4XWfinfoVo.getCapacity();
			}
			modes.add(number+"台  "+model);
			mode_types.add(model);
			wtypes.add(wtype);
			md4XWfinfoVo.setModels(modes);
			md4XWfinfoVo.setModelTypes(mode_types);
			md4XWfinfoVo.setWindTurbinCount(windTurbinCount+number);
			md4XWfinfoVo.setWtype(wtypes);
			md4XWfinfoVo.setCapacity(fcapacity + scapacity);
			windFarmMap.put(wfName, md4XWfinfoVo);
		}
	
		//--按照装机容量倒序的分页数据集----
		List<WindFarmInfoVO> farmInfos = new ArrayList<WindFarmInfoVO>();
		for(Md4xWfinfo info:gWFinalMD4xWfinfo) {
			WindFarmInfoVO  farminfo = new WindFarmInfoVO();
			farminfo.setSystemdefinedfarmid(info.getWfid());//风场id
			farminfo.setWfid(info.getWfid());//风场id
			farminfo.setName(info.getWfName());//风场名字
			farminfo.setOwnerAbb(info.getOwnerAbb());//业主归类
			farminfo.setOwner(info.getOwner());//业主全称
			farminfo.setOrganization(info.getOrganization());//业主简称
			farminfo.setWfcapacity(info.getCapacity());//风场容量
			farminfo.setModelTypes(windFarmMap.get(info.getWfName()).getModelTypes());
			farminfo.setModels(windFarmMap.get(info.getWfName()).getModels());
			farminfo.setProjectStatus(info.getProjectStatus());
			farminfo.setProvince(info.getProvince());
			farminfo.setTower(info.getTower());//塔高
			farminfo.setTowerType(info.getTowerType());//塔类型
			farminfo.setSigningDate(info.getSigningDate());//合同签定时间(并网时间?)
			farminfo.setWtypes(windFarmMap.get(info.getWfName()).getWtype());
			farminfo.setAltitudeMaximun(info.getAltitudeMaximun());//最高海拔
			farminfo.setAltitudeMinimun(info.getAltitudeMinimun());//最低海拔
			farminfo.setCity(info.getCity());
			farminfo.setCountry(info.getCountry());
			farminfo.setTerrain(info.getTerrain());//地形
			farminfo.setMainWindDirection(info.getMainWindDirection());//主风向
			farminfo.setProvinceAbb(info.getProvinceAbb());//省名，简称，如新彊维吾尔族自治区 - 新彊
			farminfo.setLongitudeWindField(info.getLongitudeWindField());//经度
			farminfo.setLatitudeWindField(info.getLatitudeWindField());//纬度
			
			if("wfcapacity".equalsIgnoreCase(sortFlag)) {//按照装机容量排序
				Double dataIntegrityValue = dataIntegrity.get(info.getWfid());
				if(dataIntegrityValue == null)
					dataIntegrityValue = 0.00d;
				farminfo.setDataIntegrity(dataIntegrityValue+"%");
			}else {//按照 数据完整度排序
				farminfo.setDataIntegrity(info.getDataIntegrity()+"%");
				farminfo.setWfcapacity(String.valueOf(windFarmMap.get(info.getWfName()).getCapacity()));
				
			}
			farmInfos.add(farminfo);
			
		}
		
		//统计符合条件的所有风场的风机总数、装机总容量
		int wtcount = 0;//符合条件的总风机数
		double wtcapacity = 0.0f;//符合条件的总装机容量
	    Set<String> wfnameSet = new HashSet<String>();
		for(Md4xWfinfo info:md4xWfinfos) {
			String wfname = info.getWfName();
			if(!wfnameSet.contains(wfname)) {//md4xWfinfos有重复的wfname，通过set集合避免重复统计
				wtcount = wtcount + windFarmMap.get(wfname).getWindTurbinCount();
				wtcapacity = wtcapacity +  windFarmMap.get(wfname).getCapacity();
				wfnameSet.add(wfname);
			}
		}
		Page<WindFarmInfoVO> p = new Page<WindFarmInfoVO>(start,totalCount, pageSize,farmInfos);
		Map<String,Object> returnData = new HashMap<String,Object>();
		returnData.put("windTurbinCount", wtcount);
		returnData.put("windTurbinCapacity", Double.valueOf(String.format("%.4f", wtcapacity/100)));
		p.setReturnData(returnData);
		
		return p;
	}

	@Override
	public List<Md4xWfinfoSummaryVO> getMd4xWfinfoSummary(String searchKey, String searchValue) {
		Condition c = Condition.newInstance();
		c.addMapCondition("searchKey", searchKey).addMapCondition("searchValue", searchValue);
		List<ResultMapData> list = resultMapDataDao.selectList("groupByProvince",c);
		List<Md4xWfinfoSummaryVO> finfoSummaryVO = new ArrayList<Md4xWfinfoSummaryVO>();
		for(ResultMapData info:list) {
			Md4xWfinfoSummaryVO vo = new Md4xWfinfoSummaryVO();
			vo.setProvince(info.getString("province"));
			vo.setWindFarmCount(info.get("windFarmCount").toString());
			vo.setWfcapacity(info.get("capacity").toString());
			finfoSummaryVO.add(vo);
		}
		return finfoSummaryVO;
	}


	@Override
	public List<WindFarmInfo> getAllFarmInfos() {
		Condition c = Condition.newInstance();
		List<ResultMapData> list = resultMapDataDao.selectList("groupAllWindFarms",c);
		List<WindFarmInfo> windFarms = new ArrayList<WindFarmInfo>();
		for(ResultMapData wf:list) {
			WindFarmInfo info = new WindFarmInfo();
			info.setWfid(wf.getString("systemdefinedfarmid"));
			info.setValue(wf.getString("name"));
			windFarms.add(info);
		}
		return windFarms;
	}
	

	@Override
	public List<WindFarmOwnerInfo> getAllOwners() {
		Condition c = Condition.newInstance();
		List<ResultMapData> list = resultMapDataDao.selectList("groupAllOwners",c);
		List<WindFarmOwnerInfo> windFarms = new ArrayList<WindFarmOwnerInfo>();
		for(ResultMapData owner:list) {
			WindFarmOwnerInfo info = new WindFarmOwnerInfo();
			info.setValue(owner.getString("owner"));
			windFarms.add(info);
		}
		return windFarms;
	}

	/**
	 * 风场类型
	 */
	@Override
	public List<WindFarmTypeInfo> getAllFarmType() {
		Condition c = Condition.newInstance();
		List<ResultMapData> list = resultMapDataDao.selectList("groupAllWindFarmType",c);
		List<WindFarmTypeInfo> windFarmType = new ArrayList<WindFarmTypeInfo>();
		for(ResultMapData wf:list) {
			WindFarmTypeInfo info = new WindFarmTypeInfo();
			info.setWfType(wf.getString("modelType"));
			info.setWfCount(Integer.parseInt(wf.get("windFarmCount")+""));
			windFarmType.add(info);
		}
		return windFarmType;
	}
	
}
