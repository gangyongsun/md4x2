package cn.com.goldwind.md4x.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



/**
 * 基于Mybatis的基础泛型DAO实现类。
 *
 * @param <T>  业务实体类型
 * @param <ID> ID类型 ，如：String、Long、Integer 等
 * @author wanguiyu
 */
public abstract class BaseDaoImpl<T, ID> extends SqlSessionDaoSupport implements BaseDao<T, ID> {
	//@Autowired
	protected SqlSession sqlSessionTemplate;
	
    public static final String SQLNAME_SEPARATOR = ".";

    public static final String SQL_INSERT = "insert";
    public static final String SQL_INSERT_SELECTIVE = "insertSelective";
    public static final String SQL_UPDATE_BY_PRIMARY_KEY = "updateByPrimaryKey";
    public static final String SQL_UPDATE_BY_PRIMARY_KEY_SELECTIVE = "updateByPrimaryKeySelective";
    public static final String SQL_SELECT_BY_PRIMARY_KEY = "selectByPrimaryKey";
    public static final String SQL_DELETE_BY_PRIMARY_KEY = "deleteByPrimaryKey";
    public static final String SQL_DELETEBYIDS = "deleteByIds";
    public static final String SQL_DELETE_BY_CONDITION = "deleteByCondition";
    public static final String SQL_SELECT_PAGE_BY = "selectPageBy";
    public static final String SQL_SELECT_PAGE_BY_CONDITION = "selectPageByCondition";
    public static final String SQL_GETCOUNT_BY_CONDITION = "getCountByCondition";
    public static final String SQL_SELECT_LIST_BY_CONDITION = "searchListByCondition";
    public static final String SQL_SELECT_BY = "selectBy";
    public static final String SQL_GETCOUNTBY = "getCountBy";

    private static final String SORT_NAME = "SORT";

    private static final String DIR_NAME = "DIR";
    /**
     * 不能用于SQL中的非法字符（主要用于排序字段名）
     */
    public static final String[] ILLEGAL_CHARS_FOR_SQL = {",", ";", " ", "\"", "%"};

    
    public SqlSession getSqlSession() {
        return sqlSessionTemplate;
    }
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSessionTemplate = sqlSession;
    }
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);
    }
    //-------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public T selectByPrimaryKey(ID id) {
        return (T) this.getSqlSession().selectOne(
                getSqlName(SQL_SELECT_BY_PRIMARY_KEY), id);

    }

    public Integer deleteByPrimaryKey(ID id) {
        return this.getSqlSession().delete(
                getSqlName(SQL_DELETE_BY_PRIMARY_KEY), id);
    }

    public Integer insert(T ob) {
        generateId(ob);
        return this.getSqlSession().insert(
                getSqlName(SQL_INSERT), ob);
    }

    public Integer insertSelective(T ob) {
        generateId(ob);
        return this.getSqlSession().insert(
                getSqlName(SQL_INSERT_SELECTIVE), ob);
    }

    public Integer updateByPrimaryKey(T ob) {
        return this.getSqlSession().update(
                getSqlName(SQL_UPDATE_BY_PRIMARY_KEY), ob);
    }

    public Integer updateByPrimaryKeySelective(T ob) {
        return this.getSqlSession().update(
                getSqlName(SQL_UPDATE_BY_PRIMARY_KEY_SELECTIVE), ob);
    }
    //-------------------------------------------------------------------------


    /**
     * 获取默认SqlMapping命名空间。
     * 使用泛型参数中业务实体类型的全限定名作为默认的命名空间。
     * 如果实际应用中需要特殊的命名空间，可由子类重写该方法实现自己的命名空间规则。
     *
     * @return 返回命名空间字符串
     */
    @SuppressWarnings("unchecked")
    protected String getDefaultSqlNamespace() {
        Class<T> clazz = ReflectGeneric.getClassGenricType(this.getClass());
        String nameSpace = clazz.getName()+"Mapper";
        return nameSpace;
    }

    /**
     * 将SqlMapping命名空间与给定的SqlMapping名组合在一起。
     *
     * @param sqlName SqlMapping名
     * @return 组合了SqlMapping命名空间后的完整SqlMapping名
     */
    protected String getSqlName(String sqlName) {
        return sqlNamespace + SQLNAME_SEPARATOR + sqlName;
    }

    /**
     * SqlMapping命名空间
     */
    private String sqlNamespace = getDefaultSqlNamespace();

    /**
     * 获取SqlMapping命名空间
     *
     * @return SqlMapping命名空间
     */
    public String getSqlNamespace() {
        return sqlNamespace;
    }

    /**
     * 设置SqlMapping命名空间。
     * 此方法只用于注入SqlMapping命名空间，以改变默认的SqlMapping命名空间，
     * 不能滥用此方法随意改变SqlMapping命名空间。
     *
     * @param sqlNamespace SqlMapping命名空间
     */
    public void setSqlNamespace(String sqlNamespace) {
        this.sqlNamespace = sqlNamespace;
    }

    /**
     * 生成主键值。
     * 默认情况下什么也不做；
     * 如果需要生成主键，需要由子类重写此方法根据需要的方式生成主键值。
     *
     * @param ob 要持久化的对象
     */
    protected void generateId(T ob) {

    }

    /* (non-Javadoc)
     * @see com.harmony.framework.dao.mybatis.IBaseGenericDAO#deleteByIds(ID[])
     */
    public Integer deleteByIds(ID[] ids) {
        return this.getSqlSession().delete(
                getSqlName(SQL_DELETEBYIDS), ids);
    }

    /* (non-Javadoc)
     * @see com.harmony.framework.dao.mybatis.IBaseGenericDAO#findPageBy(java.lang.Object, int, int, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public IGenericPage<T> selectPageBy(
            T param, int pageNo, int pageSize, String sort, String dir) {

        // 获取满足条件的记录总数，没有记录时返回空页数据
        int count = getCountBy(param);
        if (count < 1) {
            return GenericDefaultPage.emptyPage();
        }

        Map<String, Object> paramMap = null;
        try {
            paramMap = BeanMapUtil.bean2Map(param);
        } catch (Exception e) {
            throw new BaseDaoException("获取参数失败", e);
        }
        // Where过滤条件
//		paramMap.put("param", param);
        // 排序条件
        if (sort != null) {
            // 排序字段不为空，过滤其中可能存在的非法字符
            sort = filterIllegalChars(sort, ILLEGAL_CHARS_FOR_SQL);
        }
        if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(dir)) {
//			paramMap.put("sort", null);
//			paramMap.put("dir", null);
        } else {
            paramMap.put(SORT_NAME, sort);
            paramMap.put(DIR_NAME, dir);
        }
        // 分页条件
        int start = GenericDefaultPage.getStartOfPage(pageNo, pageSize) - 1;
        RowBounds rowBound = new RowBounds(start, pageSize);
        List<T> lst = this.getSqlSession().selectList(getSqlName(SQL_SELECT_PAGE_BY), paramMap, rowBound);
        return new GenericDefaultPage<T>(pageNo, pageSize, lst, count);
    }

    /**
     * @param condition
     * @return
     */
    public IGenericPage<T> selectPageBy(Condition condition) {
        Integer count = getCountByCondition(condition);
        int countInt = count == null ? 0 : count.intValue();
        // 分页条件
        int limit = condition.getLimit() == null ? GenericDefaultPage.DEFAULT_PAGE_SIZE : condition.getLimit();
        int start = condition.getStart() == null ? GenericDefaultPage.DEFAULT_PAGE_NO : condition.getStart();
        int pageNo = start / limit + 1;
        RowBounds rowBound = new RowBounds(start, limit);
        List<T> lst = this.getSqlSession().selectList(getSqlName(SQL_SELECT_LIST_BY_CONDITION), condition, rowBound);
        return new GenericDefaultPage<T>(pageNo, limit, lst, countInt);
    }

    /* (non-Javadoc)
     * @see com.harmony.framework.dao.mybatis.IBaseGenericDAO#getCountBy(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public Integer getCountBy(T param) {
        Map<String, Object> paramMap = null;
        try {
            if(param!=null)paramMap = BeanMapUtil.bean2Map(param);
        } catch (Exception e) {
            throw new BaseDaoException("获取参数失败", e);
        }
//		paramMap.put("param", param);
        return (Integer) this.getSqlSession().selectOne(
                getSqlName(SQL_GETCOUNTBY), paramMap);
    }

    /**
     * @param condition
     * @return
     */
  
    public Integer getCountByCondition(Condition condition) {
        return (Integer) this.getSqlSession().selectOne(
                getSqlName(SQL_GETCOUNT_BY_CONDITION), condition);
    }

    /* (non-Javadoc)
     * @see com.harmony.framework.dao.mybatis.IBaseGenericDAO#findListBy(java.lang.Object, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<T> selectBy(T param, String sort, String dir) {
        Map<String, Object> paramMap = null;
        try {
            paramMap = BeanMapUtil.bean2Map(param);
        } catch (Exception e) {
            throw new BaseDaoException("获取参数失败", e);
        }
        // Where过滤条件
//		paramMap.put("param", param);
        // 排序条件
        if (sort != null) {
            // 排序字段不为空，过滤其中可能存在的非法字符
            sort = filterIllegalChars(sort, ILLEGAL_CHARS_FOR_SQL);
        }
        if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(dir)) {
//			paramMap.put("sort", null);
//			paramMap.put("dir", null);
        } else {
            paramMap.put(SORT_NAME, sort);
            paramMap.put(DIR_NAME, dir);
        }
        List<T> lst = this.getSqlSession().selectList(
                getSqlName(SQL_SELECT_BY), paramMap);
        return lst;
    }

    /**
     * 从给定字符串中将指定的非法字符串数组中各字符串过滤掉。
     *
     * @param str         待过滤的字符串
     * @param filterChars 指定的非法字符串数组
     * @return 过滤后的字符串
     */
    protected String filterIllegalChars(String str, String[] filterChars) {
        String rs = str;
        if (rs != null && filterChars != null) {
            for (String fc : filterChars) {
                if (fc != null && fc.length() > 0) {
                    str = str.replaceAll(fc, "");
                }
            }
        }
        return rs;
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#insert(java.lang.String, java.lang.Object)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @return 执行结果——插入成功的记录数
     * @see org.apache.ibatis.session.SqlSession#insert(java.lang.String, java.lang.Object)
     */
    protected int insert(String statement, Object parameter) {
        return this.getSqlSession().insert(
                getSqlName(statement), parameter);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#insert(java.lang.String)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @return 执行结果——插入成功的记录数
     * @see org.apache.ibatis.session.SqlSession#insert(java.lang.String)
     */
    protected int insert(String statement) {
        return this.getSqlSession().insert(
                getSqlName(statement));
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#update(java.lang.String, java.lang.Object)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @return 执行结果——更新成功的记录数
     * @see org.apache.ibatis.session.SqlSession#update(java.lang.String, java.lang.Object)
     */
    public int update(String statement, Object parameter) {
        return this.getSqlSession().update(
                getSqlName(statement), parameter);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#update(java.lang.String)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @return 执行结果——更新成功的记录数
     * @see org.apache.ibatis.session.SqlSession#update(java.lang.String)
     */
    protected int update(String statement) {
        return this.getSqlSession().update(
                getSqlName(statement));
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#delete(java.lang.String, java.lang.Object)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @return 执行结果——删除成功的记录数
     * @see org.apache.ibatis.session.SqlSession#delete(java.lang.String, java.lang.Object)
     */
    public int delete(String statement, Object parameter) {
        return this.getSqlSession().delete(
                getSqlName(statement), parameter);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#delete(java.lang.String)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @return 执行结果——删除成功的记录数
     * @see org.apache.ibatis.session.SqlSession#delete(java.lang.String)
     */
    protected int delete(String statement) {
        return this.getSqlSession().delete(
                getSqlName(statement));
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectList(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @param rowBounds 用于分页查询的记录范围
     * @return 查询结果列表
     * @see org.apache.ibatis.session.SqlSession#selectList(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds)
     */
    protected List<?> selectList(
            String statement, Object parameter, RowBounds rowBounds) {
        return this.getSqlSession().selectList(
                getSqlName(statement), parameter, rowBounds);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectList(java.lang.String, java.lang.Object)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @return 查询结果列表
     * @see org.apache.ibatis.session.SqlSession#selectList(java.lang.String, java.lang.Object)
     */
    public List<T> selectList(String statement, Object parameter) {
        return this.getSqlSession().selectList(
                getSqlName(statement), parameter);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectList(java.lang.String)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @return 查询结果列表
     * @see org.apache.ibatis.session.SqlSession#selectList(java.lang.String)
     */
    protected List<?> selectList(String statement) {
        return this.getSqlSession().selectList(
                getSqlName(statement));
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectOne(java.lang.String, java.lang.Object)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @return 查询结果对象
     * @see org.apache.ibatis.session.SqlSession#selectOne(java.lang.String, java.lang.Object)
     */
    public T selectOne(String statement, Object parameter) {
        return this.getSqlSession().selectOne(
                getSqlName(statement), parameter);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectOne(java.lang.String)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @return 查询结果对象
     * @see org.apache.ibatis.session.SqlSession#selectOne(java.lang.String)
     */
    protected Object selectOne(String statement) {
        return this.getSqlSession().selectOne(
                getSqlName(statement));
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.Object, java.lang.String, org.apache.ibatis.session.RowBounds)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @param mapKey    数据mapKey
     * @param rowBounds 用于分页查询的记录范围
     * @return 查询结果Map
     * @see org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.Object, java.lang.String, org.apache.ibatis.session.RowBounds)
     */
    protected Map<?, ?> selectMap(
            String statement, Object parameter, String mapKey,
            RowBounds rowBounds) {
        return this.getSqlSession().selectMap(
                getSqlName(statement),
                parameter, mapKey, rowBounds);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.Object, java.lang.String)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @param mapKey    数据mapKey
     * @return 查询结果Map
     * @see org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.Object, java.lang.String)
     */
    protected Map<?, ?> selectMap(
            String statement, Object parameter, String mapKey) {
        return this.getSqlSession().selectMap(
                getSqlName(statement), parameter, mapKey);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.String)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param mapKey    数据mapKey
     * @return 查询结果Map
     * @see org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.String)
     */
    protected Map<?, ?> selectMap(String statement, String mapKey) {
        return this.getSqlSession().selectMap(
                getSqlName(statement), mapKey);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#select(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @param rowBounds 用于分页查询的记录范围
     * @param handler   结果集处理器
     * @see org.apache.ibatis.session.SqlSession#select(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler)
     */
    protected void select(
            String statement, Object parameter, RowBounds rowBounds,
            ResultHandler<?> handler) {
        this.getSqlSession().select(
                getSqlName(statement),
                parameter, rowBounds, handler);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#select(java.lang.String, java.lang.Object, org.apache.ibatis.session.ResultHandler)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param parameter 参数
     * @param handler   结果集处理器
     * @see org.apache.ibatis.session.SqlSession#select(java.lang.String, java.lang.Object, org.apache.ibatis.session.ResultHandler)
     */
    protected void select(
            String statement, Object parameter, ResultHandler<?> handler) {
        this.getSqlSession().select(
                getSqlName(statement), parameter, handler);
    }

    /**
     * 对{@link org.apache.ibatis.session.SqlSession#select(java.lang.String, org.apache.ibatis.session.ResultHandler)}的代理。
     * 将statement包装了命名空间，方便DAO子类调用。
     *
     * @param statement 映射的语句ID
     * @param handler   结果集处理器
     * @see org.apache.ibatis.session.SqlSession#select(java.lang.String, org.apache.ibatis.session.ResultHandler)
     */
    protected void select(String statement, ResultHandler<?> handler) {
        this.getSqlSession().select(
                getSqlName(statement), handler);
    }

    /**
     * 查询结果集,返回多条记录
     *
     * @param statement 映射的语句ID
     * @param c
     * @return 查询结果列表
     */
    
    public List<T> selectList(String statement, Condition c) {
        if (c.getLimit() == null && c.getStart() == null) {
            return this.getSqlSession().selectList(getSqlName(statement), c);
        }
        RowBounds rowBound = new RowBounds(c.getStart(), c.getLimit());
        return this.getSqlSession().selectList(getSqlName(statement), c, rowBound);
    }
    
    
    /**
     * 查询结果集,返回多条记录
     * @param c
     * @return 查询结果列表
     */
    public List<T> searchListByCondition(Condition c) {
        if (c.getLimit() == null && c.getStart() == null) {
            return this.getSqlSession().selectList(getSqlName(SQL_SELECT_LIST_BY_CONDITION),c);
        }
        RowBounds rowBound = new RowBounds(c.getStart(), c.getLimit());
        return this.getSqlSession().selectList(getSqlName(SQL_SELECT_LIST_BY_CONDITION), c, rowBound);
    }
    
    /**
	  * 删除记录,如果没指定查询sql，默认使用
	  * com.tinet.ccic.v3.core.dao.impl.MybatisBaseGenericDAOImpl.SQL_DELETE_BY_CONDITION
	  * 做删除
	  * @param c
	  * @return
	  */
	public Integer deleteByCondition(Condition c) {
		if (StringUtils.isEmpty(c.getStatement())) {
			c.setStatement(SQL_DELETE_BY_CONDITION);
		}
		return this.getSqlSession().delete(getSqlName(c.getStatement()), c);
	}
	
	
    public Integer getCountByObject(Object param) {
        Map<String, Object> paramMap = null;
        try {
            if(param!=null)paramMap = BeanMapUtil.bean2Map(param);
        } catch (Exception e) {
            throw new BaseDaoException("获取参数失败", e);
        }
        return (Integer) this.getSqlSession().selectOne(
                getSqlName(SQL_GETCOUNTBY), paramMap);
    }
	
	
    /**查询结果集(分页),自定义sql语句
     * @param condition
     * @return
     */
    public IGenericPage<T> selectPageBy(String statement,Condition condition) {
        Integer count = getCountByCondition("getCountByConditionWithPage",condition);
        int countInt = count == null ? 0 : count.intValue();
        // 分页条件
        int limit = condition.getLimit() == null ? GenericDefaultPage.DEFAULT_PAGE_SIZE : condition.getLimit();
        int start = condition.getStart() == null ? GenericDefaultPage.DEFAULT_PAGE_NO : condition.getStart();
        int pageNo = start / limit + 1;
        RowBounds rowBound = new RowBounds(start, limit);
        List<T> lst = this.getSqlSession().selectList(getSqlName(statement), condition, rowBound);
        return new GenericDefaultPage<T>(pageNo, limit, lst, countInt);
    }
    
  /**
   * 
   * @param statement
   * @param condition
   * @return
   */
    public Integer getCountByCondition(String statement,Condition condition) {
        return (Integer) this.getSqlSession().selectOne(
                getSqlName(statement), condition);
    }
}
