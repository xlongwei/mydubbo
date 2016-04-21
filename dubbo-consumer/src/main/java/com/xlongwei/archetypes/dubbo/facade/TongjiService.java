package com.xlongwei.archetypes.dubbo.facade;

import java.util.List;

import com.xlongwei.archetypes.dubbo.util.Result;
import com.xlongwei.archetypes.dubbo.util.SqlBuilder;
import com.xlongwei.archetypes.dubbo.util.SqlInsert;

@SuppressWarnings("rawtypes")
public interface TongjiService {
	Result<Integer> count(SqlBuilder sqlBuilder);
	Result<List> list(SqlBuilder sqlBuilder);
	Result<Object[]> sums(SqlBuilder sqlBuilder);
	Result<Integer> insert(SqlInsert sqlInsert);
	Result<Integer> update(SqlBuilder sqlBuilder);
	Result<Integer> delete(SqlBuilder sqlBuilder);
<T> Result<List<T>> list(SqlBuilder sqlBuilder, RowMapper<T> rowMapper);
	public static interface RowMapper<T> {
		T mapRow(Object[] arr);
	}
}
