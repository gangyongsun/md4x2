package cn.com.goldwind.md4x.config.aop;
/**
 * 
 * @author wangguiyu
 *
 */
public enum OperationUnit {
    /**
     * 主要操作的对象
     */
    UNKNOWN("unknown"),
    TABLE("table"),//数据表
    AWS("aws");//aws服务

    private String value;

    OperationUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}