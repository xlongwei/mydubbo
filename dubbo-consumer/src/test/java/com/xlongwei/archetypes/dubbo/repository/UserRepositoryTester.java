package com.xlongwei.archetypes.dubbo.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xlongwei.archetypes.dubbo.entity.User;
import com.xlongwei.archetypes.dubbo.Consumer;

public class UserRepositoryTester extends Consumer {
	@Autowired UserRepository userRepository;
	
	@Test
	public void findByUserName() {
		User user = userRepository.findByUserName("admin");
		System.out.println(user.getId());
	}
}
