package cn.com.goldwind.md4x.config.aop;
/**
 * 
 * @author wangguiyu
 *
 */
public enum OperationType {
    /**
     * 操作类型增,删、改、查
     */
    UNKNOWN("unknown"),
    DELETE("delete"),
    SELECT("select"),
    UPDATE("update"),
    INSERT("insert");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    OperationType(String s) {
        this.value = s;
    }
}