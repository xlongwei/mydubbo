package com.xlongwei.archetypes.dubbo.util;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体，分页大小可以从表单hidden传入，也可以在Controller里设定，或者BaseController里配置。
 * <pre><code>
 * Pager pager = new Pager();
 * pager.setPageSize(20);//default is 12.
 * if(pager.notInitialized()){
 *     int totalRows = ***;//get totalRows
 *     pager.init(totalRows);//totalRows=570,
 * }
 * pager.page(7);//jump to page:7
 * pager.pageWindow(7); pager.getStartPage(); pager.getEndPage();//set pageWindow and get page numbers to show
 * pager.getStartRow(); pager.getEndRow();//limit start,pageSize=endRow-startRow+1
 * </code></pre>
 * @author hongwei
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class Pager implements Serializable {
	private int totalRows = -1;
	private int pageSize = 12;
	private int totalPages = 1;
	private int currentPage = 1;
	private int pageWindow = 7;//页码窗口
	private List elements = null;
	private List others = null;
	private String direction = null;
	private String properties = null;

	/**
	 * 未初始化分页，请调用init初始化
	 */
	public Pager() {}
	
	/**
	 * 初始化分页，调用page(n)设置页码
	 */
	public Pager(int totalRows, int pageSize) {
		this.pageSize = pageSize;
		init(totalRows);
	}

	/**
	 * 初始化
	 */
	public void init (int totalRows) {
		this.totalRows = totalRows > 0 ? totalRows : 0;
		this.totalPages = pageSize > 0 ? this.totalRows / pageSize + (this.totalRows % pageSize > 0 ? 1 : 0) : (this.totalRows > 0 ? 1 : 0);
	}
	
	/**
	 * 如果没有初始化，则请初始化
	 */
	public boolean notInitialized() {
		return totalRows == -1;
	}
	
	/**
	 * 跳转页，从1开始
	 */
	public void page(int page) {
		if(notInitialized() && page>0) currentPage = page;
		else if(page > 0 && page <= totalPages) currentPage = page;
	}
	
	/**
	 * 设置需要显示的页码窗口大小，默认10个页码
	 */
	public void pageWindow(int pageWindow) {
		this.pageWindow = pageWindow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void setPageSize(String pageSize, int defaultPageSize) {
		try {
			this.pageSize = Integer.parseInt(pageSize);
		}catch(Exception e) {
			this.pageSize = defaultPageSize;
		}
	}

	public int getStartRow() {
		return (currentPage - 1) * pageSize;
	}
	
	public int getEndRow() {
		return currentPage * pageSize - 1;
	}
	
	public int getStartPage() {
		int pageMiddle = pageWindow / 2;
		int startPage = currentPage <= pageMiddle ? 1 : currentPage - pageMiddle;
		int endPage = startPage + pageWindow -1;
		endPage = endPage > totalPages ? totalPages : endPage;
		if(endPage==totalPages && startPage>1 && endPage-startPage<pageWindow-1) {
			int leftShift1 = pageWindow-(endPage-startPage)-1;
			int leftShift2 = startPage - 1;
			startPage -= leftShift1 < leftShift2 ? leftShift1 : leftShift2;
		}
		return startPage;
	}
	
	public int getEndPage() {
		int endPage = getStartPage() + pageWindow -1 ;
		endPage = endPage > totalPages ? totalPages : endPage;
		return endPage < 1 ? 1 : endPage;
	}
	
	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public String getDirection() {
		return direction;
	}

	/**
	 * asc或desc
	 */
	public void setDirection(String direction) {
		if("asc".equalsIgnoreCase(direction) || "desc".equalsIgnoreCase(direction)) this.direction = direction.toUpperCase();
		else System.out.println("bad direction type: "+direction);
	}

	public String getProperties() {
		return properties;
	}

	/**
	 * id或name,age
	 */
	public void setProperties(String properties) {
		this.properties = properties;
	}

	/**
	 * 获取元素列表
	 */
	public List getElements() {
		return elements;
	}

	public void setElements(List elements) {
		this.elements = elements;
	}

	/**
	 * 获取附加信息，最好将附加信息绑定到element实体上
	 */
	public List getOthers() {
		return others;
	}

	public void setOthers(List others) {
		this.others = others;
	}
}
