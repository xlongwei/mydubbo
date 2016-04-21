package com.xlongwei.archetypes.dubbo.facade;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xlongwei.archetypes.dubbo.Consumer;

public class IdServiceTester extends Consumer {
	@Autowired IdService idService;
	@Test public void next() {
		for(int i=0; i<20; i++) {
			System.out.println(idService.next());
		}
	}
}
