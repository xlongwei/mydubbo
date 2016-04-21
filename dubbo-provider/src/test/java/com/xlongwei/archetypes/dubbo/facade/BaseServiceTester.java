package com.xlongwei.archetypes.dubbo.facade;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.xlongwei.archetypes.dubbo.repository.BaseRepositoryTester;

@ContextConfiguration(locations = { "classpath:service.xml" })
public class BaseServiceTester extends BaseRepositoryTester {
	@Autowired ApplicationContext applicationContext;
	
	@SuppressWarnings("rawtypes")
	@Test public void services() {
		Map<String, BaseService> services = applicationContext.getBeansOfType(BaseService.class);
		for(String name : services.keySet()) {
			try {
				BaseService service = services.get(name);
				System.out.println(name+","+service);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
