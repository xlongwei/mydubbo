package com.xlongwei.archetypes.dubbo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GlobalConfig {
	private static final String resource = "global.properties";
	private static final Properties props = PropertiesUtil.get(resource, "utf-8");
	
	public static String getProperty(String name, String defValue) {
		String value = props.getProperty(name);
		if(StringUtil.hasLength(value)) return value;
		return defValue;
	}
	
	public static String getProperty(String name) {
		return props.getProperty(name);
	}
	
	public static List<String> getMatchNames(String regex){
		List<String> prefixNames = new ArrayList<>();
		for(String name : props.stringPropertyNames()) {
			if(name.matches(regex)) prefixNames.add(name);
		}
		return prefixNames;
	}
	
	public static String setProperty(String name, String value) {
		String property = props.getProperty(name);
		props.setProperty(name, value);
		return property;
	}
	
	public static Properties getProps() {
		return props;
	}
	
	public static String getResource() {
		return resource;
	}
}
