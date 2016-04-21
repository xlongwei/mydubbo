package com.xlongwei.archetypes.dubbo.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public class PagerUtil {
	/**
	 * Pager => Pageable
	 */
	public static Pageable pagable(Pager pager) {
		if(pager == null) return null;
		Pageable pageable = pager.getDirection()==null || pager.getProperties()==null ? 
				new PageRequest(pager.getCurrentPage()-1, pager.getPageSize()) :
					new PageRequest(pager.getCurrentPage()-1, pager.getPageSize(), Direction.valueOf(pager.getDirection()), pager.getProperties().split(","));
		return pageable;
	}
	
	/**
	 * Page => PageObject
	 */
	public static <T> PageObject<T> pageObject(Page<T> page) {
		return new PageObject<T>((int)page.getTotalElements(), page.getContent());
	}
	
	/**
	 * PageObject => Pager
	 */
	public static <T> Pager pager(PageObject<T> pageObject, Pager pager) {
		if(pager.notInitialized()) pager.init(pageObject.getTotal());
		pager.setElements(pageObject.getList());
		return pager;
	}
	
	/**
	 * Pager => limit
	 */
	public static String limit(Pager pager) {
		if(pager == null) return null;
		return pager.getCurrentPage() == 1 ? ""+pager.getPageSize() : ""+pager.getStartRow()+","+pager.getPageSize();
	}
}
