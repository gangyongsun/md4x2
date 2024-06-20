package cn.com.goldwind.md4x.mybatis;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 分页对象. 包含数据及分页信息.

 * @author wangguiyu

 */
public class Page<T> implements java.io.Serializable {
	private static final long serialVersionUID = 2442779466291470277L;
	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MIN_PAGE_SIZE = 1;
	public static final int MAX_PAGE_SIZE = 100;
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}

	/**
	 * 当前页第一条数据的位置,从0开始
	 */
	private int start;

	/**
	 * 当前页码
	 */
	private int pageNo;

	/**
	 * 每页的记录数
	 */
	private int pageSize = 10;

	/**
	 * 当前页中存放的记录
	 */
	private List<T> data;

	/**
	 * 总记录数
	 */
	private int totalCount;
	
	private Map<String,Object> returnData;

	/**
	 * 构造方法，只构造空页
	 */
	public Page() {
		this(0, 0, 10, new ArrayList<T>());
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * 默认构造方法
	 * 
	 * @param start 本页数据在数据库中的起始位置
	 * @param totalSize 数据库中总记录条数
	 * @param pageSize 本页容量
	 * @param data 本页包含的数据
	 */
	public Page(int start, int totalSize, int pageSize, List<T> data) {
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalSize;
		this.data = data;
	}

	/**
	 * 默认构造方法
	 * 
	 * @param start 本页数据在数据库中的起始位置
	 * @param totalSize 数据库中总记录条数
	 * @param pageSize 本页容量
	 * @param data 本页包含的数据
	 */
	public Page(List<T> list) {
		if (list != null) {
			this.pageSize = 0;
			this.start = 0;
			this.totalCount = list.size();
			this.data = list;
		}
	}

	/**
	 * 取数据库中包含的总记录数
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 设置数据库中包含的总记录数
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 取总页数。
	 */
	public int getTotalPageCount() {
		if (totalCount == 0) {
			return 0;
		} else {
			return ((totalCount - 1) / pageSize) + 1;
		}
	}

	/**
	 * 取每页数据容量
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页中的记录
	 */
	public List<T> getResult() {
		return data;
	}

	/**
	 * 取当前页码,页码从1开始
	 */
	public int getPageNo() {
		if (start == 0) {
			pageNo = 1;
		} else {
			int curPage = (start / pageSize) + 1;
			if (curPage > this.getTotalPageCount()) {
				curPage = this.getTotalPageCount();
			}
			pageNo = curPage;
		}
		return pageNo;
	}

	/**
	 * 是否有下一页
	 */
	public boolean hasNextPage() {
		return (this.getPageNo() < this.getTotalPageCount());
	}

	/**
	 * 是否有上一页
	 */
	public boolean hasPreviousPage() {
		return (this.getPageNo() > 1);
	}

//	@Transient
//	public String getPageBar() {
//		return barRender.render(this);
//	}

//	public List<T> getData() {
//		return data;
//	}

	@Transient
	public HttpHeaders getPageHeaders() {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> pagination = new HashMap<String,Object>();
		pagination.put("pageNo", this.getPageNo());
		pagination.put("pageSize", this.pageSize);
		pagination.put("pageCount", this.getTotalPageCount());
		pagination.put("totalCount", this.totalCount);

		try {
			headers.set("X-Pagination", mapper.writeValueAsString(pagination));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return headers;
	}

	public Map<String, Object> getReturnData() {
		return returnData;
	}

	public void setReturnData(Map<String, Object> returnData) {
		this.returnData = returnData;
	}

}
