package cn.com.goldwind.md4x.business.entity.datacenter;

public class Md4xWfinfo {
    private String province;

    private String provinceAbb;

    private String wfid;

    private String wfName;

    private String owner;

    private String contractNo;

    private String model;

    private String number;

    private String capacity;

    private String modelType;

    private String wtype;

    private String region;

    private String organization;

    private String ownerAbb;

    private String area;

    private String projectStatus;

    private Double installedCapacity;

    private String signingDate;

    private String tower;

    private String towerType;

    private String projectStage;

    private String country;

    private String city;

    private String longitudeWindField;

    private String latitudeWindField;

    private String windZone;

    private String terrain;

    private String altitudeMaximun;

    private String altitudeMinimun;

    private String temperatureMaximun;

    private String temperatureMinimun;

    private Float annualAverageThunderstorms;

    private String mainWindDirection;

    private Float annualAverageWindSpeed;

    private String updatedTime;
    
	private double dataIntegrity;//风场数据完整度

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getProvinceAbb() {
        return provinceAbb;
    }

    public void setProvinceAbb(String provinceAbb) {
        this.provinceAbb = provinceAbb == null ? null : provinceAbb.trim();
    }

    public String getWfid() {
        return wfid;
    }

    public void setWfid(String wfid) {
        this.wfid = wfid == null ? null : wfid.trim();
    }

    public String getWfName() {
        return wfName;
    }

    public void setWfName(String wfName) {
        this.wfName = wfName == null ? null : wfName.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo == null ? null : contractNo.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity == null ? null : capacity.trim();
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType == null ? null : modelType.trim();
    }

    public String getWtype() {
        return wtype;
    }

    public void setWtype(String wtype) {
        this.wtype = wtype == null ? null : wtype.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }

    public String getOwnerAbb() {
        return ownerAbb;
    }

    public void setOwnerAbb(String ownerAbb) {
        this.ownerAbb = ownerAbb == null ? null : ownerAbb.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus == null ? null : projectStatus.trim();
    }

    public Double getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(Double installedCapacity) {
        this.installedCapacity = installedCapacity;
    }

    public String getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(String signingDate) {
        this.signingDate = signingDate == null ? null : signingDate.trim();
    }

    public String getTower() {
        return tower;
    }

    public void setTower(String tower) {
        this.tower = tower == null ? null : tower.trim();
    }

    public String getTowerType() {
        return towerType;
    }

    public void setTowerType(String towerType) {
        this.towerType = towerType == null ? null : towerType.trim();
    }

    public String getProjectStage() {
        return projectStage;
    }

    public void setProjectStage(String projectStage) {
        this.projectStage = projectStage == null ? null : projectStage.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getLongitudeWindField() {
        return longitudeWindField;
    }

    public void setLongitudeWindField(String longitudeWindField) {
        this.longitudeWindField = longitudeWindField == null ? null : longitudeWindField.trim();
    }

    public String getLatitudeWindField() {
        return latitudeWindField;
    }

    public void setLatitudeWindField(String latitudeWindField) {
        this.latitudeWindField = latitudeWindField == null ? null : latitudeWindField.trim();
    }

    public String getWindZone() {
        return windZone;
    }

    public void setWindZone(String windZone) {
        this.windZone = windZone == null ? null : windZone.trim();
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain == null ? null : terrain.trim();
    }

    public String getAltitudeMaximun() {
        return altitudeMaximun;
    }

    public void setAltitudeMaximun(String altitudeMaximun) {
        this.altitudeMaximun = altitudeMaximun == null ? null : altitudeMaximun.trim();
    }

    public String getAltitudeMinimun() {
        return altitudeMinimun;
    }

    public void setAltitudeMinimun(String altitudeMinimun) {
        this.altitudeMinimun = altitudeMinimun == null ? null : altitudeMinimun.trim();
    }

    public String getTemperatureMaximun() {
        return temperatureMaximun;
    }

    public void setTemperatureMaximun(String temperatureMaximun) {
        this.temperatureMaximun = temperatureMaximun == null ? null : temperatureMaximun.trim();
    }

    public String getTemperatureMinimun() {
        return temperatureMinimun;
    }

    public void setTemperatureMinimun(String temperatureMinimun) {
        this.temperatureMinimun = temperatureMinimun == null ? null : temperatureMinimun.trim();
    }

    public Float getAnnualAverageThunderstorms() {
        return annualAverageThunderstorms;
    }

    public void setAnnualAverageThunderstorms(Float annualAverageThunderstorms) {
        this.annualAverageThunderstorms = annualAverageThunderstorms;
    }

    public String getMainWindDirection() {
        return mainWindDirection;
    }

    public void setMainWindDirection(String mainWindDirection) {
        this.mainWindDirection = mainWindDirection == null ? null : mainWindDirection.trim();
    }

    public Float getAnnualAverageWindSpeed() {
        return annualAverageWindSpeed;
    }

    public void setAnnualAverageWindSpeed(Float annualAverageWindSpeed) {
        this.annualAverageWindSpeed = annualAverageWindSpeed;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime == null ? null : updatedTime.trim();
    }

	public double getDataIntegrity() {
		return dataIntegrity;
	}

	public void setDataIntegrity(double dataIntegrity) {
		this.dataIntegrity = dataIntegrity;
	}
}