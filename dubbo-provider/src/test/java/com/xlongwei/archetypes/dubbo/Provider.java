package com.xlongwei.archetypes.dubbo;

import java.io.IOException;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.xlongwei.archetypes.dubbo.facade.BaseServiceTester;

@ContextConfiguration(locations = { "classpath:provider.xml" })
public class Provider extends BaseServiceTester {
	@Test
	public void provide() throws IOException {
		System.out.println("Dubbo service server started!");
		System.in.read();
		System.out.println("Provider Exit");
	}
}
