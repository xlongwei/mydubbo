package com.xlongwei.archetypes.dubbo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class SpecUtil {
	/**
	 * 将SpecParam转换为Specification
	 */
	public static <T> Specification<T> spec(final SpecParam<T> specParam) {
    	return new Specification<T>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(specParam!=null && specParam.getSpecs().size() > 0) {
					List<Predicate> predicates = new ArrayList<>(specParam.getSpecs().size());
					for(Object[] specTuple : specParam.getSpecs()) {
						Path path = getPath(root, specTuple[0].toString());
						switch(specTuple[1].toString()) {
						case "eq":
							predicates.add(cb.equal(path, specTuple[2]));
							break;
						case "ne":
							predicates.add(cb.notEqual(path, specTuple[2]));
							break;
						case "like":
							predicates.add(cb.like(path, "%"+(String)specTuple[2]+"%"));
							break;
						case "in":
							predicates.add(path.in((Collection)specTuple[2]));
							break;
						case "notIn":
							predicates.add(cb.not(path.in((Collection)specTuple[2])));
							break;
						case "isNull":
							predicates.add(cb.isNull(path));
							break;
						case "between":
							Object[] value = (Object[])specTuple[2];
							if(!isEmpty(value[0])) {
								if(!isEmpty(value[1])) {
									predicates.add(cb.between(path, (Comparable)value[0], (Comparable)value[1]));
								}else {
									predicates.add(cb.greaterThanOrEqualTo(path, (Comparable)value[0]));
								}
							}else if(!isEmpty(value[1])){
								predicates.add(cb.lessThanOrEqualTo(path, (Comparable)value[1]));
							}
							break;
						}
					}
					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				return cb.conjunction();
			}
		};
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
    private static Path<?> getPath(Root<?> root, String fieldName) {
    	Path<?> path = null;  
        if(fieldName.contains(".")){  
            String[] names = fieldName.split("[.]");  
            path = root.get(names[0]);  
            for (int i = 1; i < names.length; i++) {  
                path = path.get(names[i]);  
            }  
        }else{  
            path = root.get(fieldName);  
        }
        return path;
    }
}
