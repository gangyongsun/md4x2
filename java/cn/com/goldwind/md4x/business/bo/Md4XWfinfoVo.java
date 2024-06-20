package cn.com.goldwind.md4x.business.bo;

import java.util.List;
import java.util.Set;

public class Md4XWfinfoVo {
	private List<String> models;     //10台,115/220 & 33台,77/1500
	private List<String> modelTypes;//125/1500,133/2000
	private Integer windTurbinCount;//风机总数
	private Set<String> wtype;//3X00,5x00
	private double capacity;//装机总容量
	private double dataIntegrity;//风场数据完整度
	
	public List<String> getModels() {
		return models;
	}
	public void setModels(List<String> models) {
		this.models = models;
	}
	public List<String> getModelTypes() {
		return modelTypes;
	}
	public void setModelTypes(List<String> modelTypes) {
		this.modelTypes = modelTypes;
	}
	public Integer getWindTurbinCount() {
		return windTurbinCount;
	}
	public void setWindTurbinCount(Integer windTurbinCount) {
		this.windTurbinCount = windTurbinCount;
	}
	public Set<String> getWtype() {
		return wtype;
	}
	public void setWtype(Set<String> wtype) {
		this.wtype = wtype;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getDataIntegrity() {
		return dataIntegrity;
	}
	public void setDataIntegrity(double dataIntegrity) {
		this.dataIntegrity = dataIntegrity;
	}


}
