package cn.com.goldwind.md4x.mybatis;

import java.util.List;


/**
 * 
 * @ClassName: BaseDao 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author wangguiyu
 * @date 2018年5月2日 上午10:24:24 
 *
 * @param <T>
 * @param <ID>
 */
public interface BaseDao<T, ID> {

	/**
	 * 保存（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public Integer insert(T ob);

	/**
	 * 保存（持久化）对象,对字段选择性保存，当某字段为null时，此字段不保存
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public Integer insertSelective(T ob);

	/**
	 * 更新（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public Integer updateByPrimaryKey(T ob);

	/**
	 * 更新（持久化）对象,对字段选择性更新，当某字段为null时，此字段不更新
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public Integer updateByPrimaryKeySelective(T ob);

	/**
	 * 获取指定的唯一标识符对应的持久化对象
	 *
	 * @param id 指定的唯一标识符
	 * @return 指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。
	 */
	public T selectByPrimaryKey(ID id);

	/**
	 * 删除指定的唯一标识符对应的持久化对象
	 *
	 * @param id 指定的唯一标识符
	 * @return 删除的对象数量
	 */
	public Integer deleteByPrimaryKey(ID id);

	/**
	 * 删除指定的唯一标识符数组对应的持久化对象
	 *
	 * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
	 */
	public Integer deleteByIds(ID[] ids);

	/**
	 * 分页查询
	 * 
	 * @param param 查询参数
	 * @param pageNo 要查询的页号
	 * @param pageSize 每页数据个数
	 * @param sort 排序字段名
	 * @param dir 排序方式（升序(asc)或降序(desc)
	 * @return 查询结果分页数据
	 */
	public IGenericPage<T> selectPageBy(T param, int pageNo, int pageSize, String sort, String dir);

	/**
	 * 分页查询
	 * 
	 * @param condition
	 * @return 查询结果分页数据
	 */
	public IGenericPage<T> selectPageBy(Condition condition);

	/**
	 * 获取条件下的数量
	 * 
	 * @param condition
	 * @return 查询结果分页数据
	 */
	public Integer getCountByCondition(Condition condition);;
	public Integer getCountByCondition(String statement,Condition condition);

	/**
	 * 获取满足查询参数条件的数据总数
	 * 
	 * @param param 查询参数
	 * @return 数据总数
	 */
	public Integer getCountBy(T param);

	/**
	 * 不分页查询
	 * 
	 * @param param 查询参数
	 * @param sort 排序字段名
	 * @param dir 排序方式（升序(asc)或降序(desc)
	 * @return 查询结果列表
	 */
	// public List<T> selectBy(T param, String sort, String dir);

	/**
	 * 查询结果集,返回多条记录
	 * 
	 * @param statement 映射的语句ID
	 * @param parameter 参数
	 * @return 查询结果列表
	 */
	public List<T> selectList(String statement, T parameter);

	/**
	 * 返回一条记录
	 * 
	 * @param statement 映射的语句ID
	 * @param parameter 参数
	 * @return 查询结果对象
	 */
	public T selectOne(String statement, T parameter);

	/**
	 * 查询结果集,返回多条记录
	 * 
	 * @param statement 映射的语句ID
	 * @param parameter 参数
	 * @return 查询结果列表
	 */
	public List<T> selectList(String statement, Condition c);

	/**
	 * @param statement 映射的语句ID
	 * @param parameter 参数
	 * @return 执行结果——删除成功的记录数
	 */
	public int delete(String statement, Object parameter);

	/**
	 * 查询结果集,返回多条记录
	 * 
	 * @param c
	 * @return 查询结果列表
	 */
	public List<T> searchListByCondition(Condition c);

	public Integer deleteByCondition(Condition c);

	/**
	 * 更新
	 * 
	 * @param statement 映射的语句ID
	 * @param parameter 参数
	 * @return 执行结果——更新成功的记录数
	 * @return
	 */
	public int update(String statement, Object parameter);

	/**
	 * 查询结果分页,
	 * 
	 * @param statement 自定义语句id
	 * @param condition
	 * @return
	 */
	public IGenericPage<T> selectPageBy(String statement, Condition condition);

}
