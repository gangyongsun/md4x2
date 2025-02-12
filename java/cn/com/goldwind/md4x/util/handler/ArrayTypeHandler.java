package cn.com.goldwind.md4x.util.handler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;

public class ArrayTypeHandler extends BaseTypeHandler<Object[]> {
	private static final String TYPE_NAME_VARCHAR = "varchar";
	private static final String TYPE_NAME_INTEGER = "integer";
	private static final String TYPE_NAME_BOOLEAN = "boolean";
	private static final String TYPE_NAME_NUMERIC = "numeric";

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object[] parameter, JdbcType jdbcType)
			throws SQLException {

		/*
		 * 这是ibatis时的做法 StringBuilder arrayString = new StringBuilder("{");
		 * 
		 * for (int j = 0, l = parameter.length; j &lt; l; j++) {
		 * arrayString.append(parameter[j]); if (j &lt; l - 1) {
		 * arrayString.append(","); } }
		 * 
		 * arrayString.append("}");
		 * 
		 * ps.setString(i, arrayString.toString());
		 */

		String typeName = null;
		if (parameter instanceof Integer[]) {
			typeName = TYPE_NAME_INTEGER;
		} else if (parameter instanceof String[]) {
			typeName = TYPE_NAME_VARCHAR;
		} else if (parameter instanceof Boolean[]) {
			typeName = TYPE_NAME_BOOLEAN;
		} else if (parameter instanceof Double[]) {
			typeName = TYPE_NAME_NUMERIC;
		}

		if (typeName == null) {
			throw new TypeException(
					"ArrayTypeHandler parameter typeName error, your type is " + parameter.getClass().getName());
		}
		// 这3行是关键的代码，创建Array，然后ps.setArray(i, array)就可以了
		Connection conn = ps.getConnection();
		Array array = conn.createArrayOf(typeName, parameter);
		ps.setArray(i, array);
	}

	@Override
	public Object[] getNullableResult(ResultSet rs, String columnName) throws SQLException {

		return getArray(rs.getArray(columnName));
	}

	@Override
	public Object[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

		return getArray(rs.getArray(columnIndex));
	}

	@Override
	public Object[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

		return getArray(cs.getArray(columnIndex));
	}

	private Object[] getArray(Array array) {

		if (array == null) {
			return null;
		}

		try {
			return (Object[]) array.getArray();
		} catch (Exception e) {
		}

		return null;
	}
}
