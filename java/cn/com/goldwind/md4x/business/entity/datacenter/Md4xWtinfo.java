package cn.com.goldwind.md4x.business.entity.datacenter;

public class Md4xWtinfo {
    private String wfid;

    private String wfName;

    private String contractNo;

    private String wtid;

    private Integer protocolId;

    private String wtName;

    private String wtNarrate;

    public String getWtName() {
        return wtName;
    }

    public void setWtName(String wtName) {
        this.wtName = wtName == null ? null : wtName.trim();
    }

    public String getWtNarrate() {
        return wtNarrate;
    }

    public void setWtNarrate(String wtNarrate) {
        this.wtNarrate = wtNarrate == null ? null : wtNarrate.trim();
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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo == null ? null : contractNo.trim();
    }

    public String getWtid() {
        return wtid;
    }

    public void setWtid(String wtid) {
        this.wtid = wtid == null ? null : wtid.trim();
    }

    public Integer getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }
}