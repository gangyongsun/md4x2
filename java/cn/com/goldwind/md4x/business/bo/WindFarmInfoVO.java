package cn.com.goldwind.md4x.business.bo;

import java.util.List;
import java.util.Set;

public class WindFarmInfoVO {
    private String id;

	private String parentid;

    private String owner;

    private String operator;

    private String farmtype;

    private String windclass;

    private String climate;

    private String landform;

    private String isautosyn;

    private String otherattr;

    private String systemdefinedfarmid;

    private String system;

    private String elevation;
    
    private String province;//省份
    private String wfid;//风场id
    private String name;//风场名字
    private String region;
    private String organization;//业主，页面上展示用
    private String ownerAbb;//业主简称，搜索用
    private String projectStatus;//项目状态
    private String wfcapacity = "4万kw";//装机容量
    private List<String> models;     //10台,115/220 & 33台,77/1500
	private List<String> modelTypes;//1.5MW,2.5MW
	private String tower;//轮毂高度
	private String towerType;
	private String signingDate;
	private Set<String> wtypes;
	private String altitudeMinimun; //海拔高度最小值 **
	private String altitudeMaximun;//海拔高度最大值  **
	private String mainWindDirection;//主风向  **
	private String terrain;//地形**
	private String provinceAbb;//省
	private String city;//城市*
	private String country;//国家*
	private String longitudeWindField;//经度
	private String latitudeWindField;//纬度
	private String dataIntegrity;//风场7s数据完整度
  
	public String getWfcapacity() {
		return wfcapacity;
	}

	public void setWfcapacity(String wfcapacity) {
		this.wfcapacity = wfcapacity;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getFarmtype() {
        return farmtype;
    }

    public void setFarmtype(String farmtype) {
        this.farmtype = farmtype == null ? null : farmtype.trim();
    }


    public String getWindclass() {
        return windclass;
    }

    public void setWindclass(String windclass) {
        this.windclass = windclass == null ? null : windclass.trim();
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate == null ? null : climate.trim();
    }

    public String getLandform() {
        return landform;
    }

    public void setLandform(String landform) {
        this.landform = landform == null ? null : landform.trim();
    }

    public String getIsautosyn() {
        return isautosyn;
    }

    public void setIsautosyn(String isautosyn) {
        this.isautosyn = isautosyn == null ? null : isautosyn.trim();
    }

    public String getOtherattr() {
        return otherattr;
    }

    public void setOtherattr(String otherattr) {
        this.otherattr = otherattr == null ? null : otherattr.trim();
    }

    public String getSystemdefinedfarmid() {
        return systemdefinedfarmid;
    }

    public void setSystemdefinedfarmid(String systemdefinedfarmid) {
        this.systemdefinedfarmid = systemdefinedfarmid == null ? null : systemdefinedfarmid.trim();
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system == null ? null : system.trim();
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation == null ? null : elevation.trim();
    }

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

	public String getWfid() {
		return wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOwnerAbb() {
		return ownerAbb;
	}

	public void setOwnerAbb(String ownerAbb) {
		this.ownerAbb = ownerAbb;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getTower() {
		return tower;
	}

	public void setTower(String tower) {
		this.tower = tower;
	}

	public String getTowerType() {
		return towerType;
	}

	public void setTowerType(String towerType) {
		this.towerType = towerType;
	}

	public String getSigningDate() {
		return signingDate;
	}

	public void setSigningDate(String signingDate) {
		this.signingDate = signingDate;
	}

	public Set<String> getWtypes() {
		return wtypes;
	}

	public void setWtypes(Set<String> wtypes) {
		this.wtypes = wtypes;
	}

	public String getAltitudeMinimun() {
		return altitudeMinimun;
	}

	public void setAltitudeMinimun(String altitudeMinimun) {
		this.altitudeMinimun = altitudeMinimun;
	}

	public String getAltitudeMaximun() {
		return altitudeMaximun;
	}

	public void setAltitudeMaximun(String altitudeMaximun) {
		this.altitudeMaximun = altitudeMaximun;
	}

	public String getMainWindDirection() {
		return mainWindDirection;
	}

	public void setMainWindDirection(String mainWindDirection) {
		this.mainWindDirection = mainWindDirection;
	}

	public String getTerrain() {
		return terrain;
	}

	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}

	public String getProvinceAbb() {
		return provinceAbb;
	}

	public void setProvinceAbb(String provinceAbb) {
		this.provinceAbb = provinceAbb;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLongitudeWindField() {
		return longitudeWindField;
	}

	public void setLongitudeWindField(String longitudeWindField) {
		this.longitudeWindField = longitudeWindField;
	}

	public String getLatitudeWindField() {
		return latitudeWindField;
	}

	public void setLatitudeWindField(String latitudeWindField) {
		this.latitudeWindField = latitudeWindField;
	}

	public String getDataIntegrity() {
		return dataIntegrity;
	}

	public void setDataIntegrity(String dataIntegrity) {
		this.dataIntegrity = dataIntegrity;
	}

	


}