package cn.com.goldwind.md4x.business.bo;

/**
 * 风场信息汇总
 * @author wangguiyu
 *
 */
public class Md4xWfinfoSummaryVO {
	private String province;//省份
	private String windFarmCount;//风场风机 总数量
	private String wfcapacity;//风场装机 总容量
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

	public String getWindFarmCount() {
		return windFarmCount;
	}
	public void setWindFarmCount(String windFarmCount) {
		this.windFarmCount = windFarmCount;
	}
	public String getWfcapacity() {
		return wfcapacity;
	}
	public void setWfcapacity(String wfcapacity) {
		this.wfcapacity = wfcapacity;
	}
}
