package cn.com.goldwind.md4x.util.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
/**
 * mapper里json型字段到类的映射。
 * 用法一:
 * 入库：#{jsonDataField, typeHandler=cn.com.goldwind.md4x.util.handler.JsonTypeHandler}
 * 出库：
 * <resultMap>
 * <result property="jsonDataField" column="json_data_field" javaType="com.xxx.MyClass" typeHandler="cn.com.goldwind.md4x.util.handler.JsonTypeHandler"/>
 * </resultMap>
 *
 * 用法二：
 * 1）在mybatis-config.xml中指定handler:
 *      <typeHandlers>
 *              <typeHandler handler="cn.com.goldwind.md4x.util.handler.JsonTypeHandler" javaType="com.xxx.MyClass"/>
 *      </typeHandlers>
 * 2)在MyClassMapper.xml里直接select/update/insert。
 *
 *
 */
public class JsonTypeHandler<T extends Object> extends BaseTypeHandler<T> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private Class<T> clazz;
 
    public JsonTypeHandler(Class<T> clazz) {
        if (clazz == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.clazz = clazz;
    }
 
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.toJson(parameter));
    }
 
    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.toObject(rs.getString(columnName), clazz);
    }
 
    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.toObject(rs.getString(columnIndex), clazz);
    }
 
    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.toObject(cs.getString(columnIndex), clazz);
    }
 
    private String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    private T toObject(String content, Class<?> clazz) {
        if (content != null && !content.isEmpty()) {
            try {
                return (T) mapper.readValue(content, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
 
    static {
    	
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);//禁止把POJO中值为null的字段映射到json字符串中
        mapper.setSerializationInclusion(Include.NON_NULL);////设置输出:包含的属性不能为空   
      //Include.Include.ALWAYS 默认
      //Include.NON_DEFAULT 属性为默认值不序列化
      //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
      //Include.NON_NULL 属性为NULL 不序列化
    }
    
}
