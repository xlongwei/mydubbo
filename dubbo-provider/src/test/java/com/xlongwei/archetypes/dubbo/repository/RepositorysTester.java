package com.xlongwei.archetypes.dubbo.repository;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class RepositorysTester extends BaseRepositoryTester {
	@Autowired ApplicationContext applicationContext;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void repository() {
		Map<String, BaseRepository> repositorys = applicationContext.getBeansOfType(BaseRepository.class);
		int total = 0;
		for(String name : repositorys.keySet()) {
			try {
				BaseRepository repository = repositorys.get(name);
				long count = repository.count();
				System.out.println(name+" has "+count+" records");
				total += count;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(repositorys.size()+" repositorys, "+total+" records.");
	}
}
