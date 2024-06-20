package cn.com.goldwind.md4x.business.entity.datamart;

public class SysAwsGlue {
    private Integer id;

    private String workflowName;

    private String workflowRunId;

    private String triggerName;

    private String crawlerName;

    private String databaseName;

    private String tableName;

    private String datasetId;

    private String datasetScope;

    private String datasetOwner;

    private Boolean delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName == null ? null : workflowName.trim();
    }

    public String getWorkflowRunId() {
        return workflowRunId;
    }

    public void setWorkflowRunId(String workflowRunId) {
        this.workflowRunId = workflowRunId == null ? null : workflowRunId.trim();
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName == null ? null : triggerName.trim();
    }

    public String getCrawlerName() {
        return crawlerName;
    }

    public void setCrawlerName(String crawlerName) {
        this.crawlerName = crawlerName == null ? null : crawlerName.trim();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName == null ? null : databaseName.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId == null ? null : datasetId.trim();
    }

    public String getDatasetScope() {
        return datasetScope;
    }

    public void setDatasetScope(String datasetScope) {
        this.datasetScope = datasetScope == null ? null : datasetScope.trim();
    }

    public String getDatasetOwner() {
        return datasetOwner;
    }

    public void setDatasetOwner(String datasetOwner) {
        this.datasetOwner = datasetOwner == null ? null : datasetOwner.trim();
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }
}