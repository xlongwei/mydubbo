package com.xlongwei.archetypes.dubbo.repository;

import java.util.List;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xlongwei.archetypes.dubbo.util.Pager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:cache.xml", "classpath:repository.xml" })
public class BaseRepositoryTester {
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected void printList(List<?> list) {
		if(list==null || list.size()==0) return;
		
		for(Object item:list) {
			log.info(JSON.toJSONString(item));
		}
		log.info("list size="+list.size());
	}
	
	protected void printPage(Page<?> page) {
		if(page==null || page.getNumberOfElements()==0) {
			log.info("page is empty or null");
			return;
		}
		printList(page.getContent());
	}
	
	protected void printPager(Pager pager) {
		if(pager==null || pager.getElements()==null || pager.getElements().size()==0) {
			log.info("pager is empty or null");
			return;
		}
		printList(pager.getElements());
	}
}
