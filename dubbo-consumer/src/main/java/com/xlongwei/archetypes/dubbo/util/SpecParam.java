package com.xlongwei.archetypes.dubbo.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class SpecParam<T> implements Serializable {
	List<Object[]> specs = new ArrayList<Object[]>();
	
	public SpecParam<T> eq(String fieldName, Object value) {
		if(!isEmpty(fieldName) && !isEmpty(value)) {
			removeExist(fieldName);
			specs.add(new Object[] {fieldName, "eq", value});
		}
		return this;
	}
	
	public SpecParam<T> ne(String fieldName, Object value) {
		if(!isEmpty(fieldName) && !isEmpty(value)) {
			removeExist(fieldName);
			specs.add(new Object[] {fieldName, "ne", value});
		}
		return this;
	}
	
	public SpecParam<T> like(String fieldName, String value) {
		if(!isEmpty(fieldName) && !isEmpty(value)) {
			removeExist(fieldName);
			specs.add(new Object[] {fieldName, "like", value});
		}
		return this;
	}
	
	public SpecParam<T> between(String fieldName, Comparable<?> value, Comparable<?> value2) {
		if(!isEmpty(fieldName) && !isEmpty(value) || !isEmpty(value2)) {
			removeExist(fieldName);
			specs.add(new Object[] {fieldName, "between", new Object[] {value, value2}});
		}
		return this;
	}
	
	public SpecParam<T> in(String fieldName, Collection<?> value) {
		if(!isEmpty(fieldName) && !isEmpty(value)) {
			removeExist(fieldName);
			specs.add(new Object[] {fieldName, "in", value});
		}
		return this;
	}
	
	public SpecParam<T> notIn(String fieldName, Collection<?> value) {
		if(!isEmpty(fieldName) && !isEmpty(value)) {
			removeExist(fieldName);
			specs.add(new Object[] {fieldName, "notIn", value});
		}
		return this;
	}
	
	public SpecParam<T> isNull(String fieldName) {
		removeExist(fieldName);
		specs.add(new Object[] {fieldName, "isNull"});
		return this;
	}
	
    private static boolean isEmpty(Object value) {
    	if(value == null) return true;
    	if("".equals(value)) return true;
    	if(value instanceof Collection)
    		return ((Collection<?>)value).size()==0;
    	if(value instanceof String)
    		return ((String)value).trim().length()==0;
    	return false;
    }
    private void removeExist(String fieldName) {
    	for(int i=0; i<specs.size(); i++) {
    		Object[] spec = specs.get(i);
    		if(fieldName.equals(spec[0].toString())) {
    			specs.remove(i);
    			break;
    		}
    	}
    }

	public List<Object[]> getSpecs() {
		return specs;
	}

	public void setSpecs(List<Object[]> specs) {
		this.specs = specs;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		for(Object[] spec : specs) {
			for(Object item : spec) {
				toString.append(item.toString());
				toString.append(' ');
			}
			toString.setCharAt(toString.length()-1, ',');
		}
		if(specs.size()>0) toString.deleteCharAt(toString.length()-1);
		return toString.toString();
	}
}
