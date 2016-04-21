package com.xlongwei.archetypes.dubbo.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlongwei.archetypes.dubbo.facade.TongjiService;
import com.xlongwei.archetypes.dubbo.repository.TongjiRepository;
import com.xlongwei.archetypes.dubbo.util.Result;
import com.xlongwei.archetypes.dubbo.util.SqlBuilder;
import com.xlongwei.archetypes.dubbo.util.SqlInsert;

@Service("tongjiService")
@SuppressWarnings("rawtypes")
public class TongjiServiceImpl implements TongjiService {
	@Autowired private TongjiRepository tongjiRepository;

	@Override
	public Result<Integer> count(SqlBuilder sqlBuilder) {
		try {
			int count = tongjiRepository.count(sqlBuilder.count());
			return Result.newSuccess(count);
		}catch (Exception e) {
			return Result.newFailure(1, e.getMessage());
		}
	}

	@Override
	public Result<List> list(SqlBuilder sqlBuilder) {
		try {
			List list = tongjiRepository.list(sqlBuilder.sql());
			return Result.newSuccess(list);
		}catch (Exception e) {
			return Result.newFailure(1, e.getMessage());
		}
	}

	@Override
	public Result<Object[]> sums(SqlBuilder sqlBuilder) {
		try {
			Object[] sums = tongjiRepository.sums(sqlBuilder.sql());
			return Result.newSuccess(sums);
		}catch (Exception e) {
			return Result.newFailure(1, e.getMessage());
		}
	}

	@Override
	public Result<Integer> insert(SqlInsert sqlInsert) {
		try {
			int update = tongjiRepository.update(sqlInsert.toString());
			return Result.newSuccess(update);
		}catch(Exception e) {
			return Result.newFailure(1, e.getMessage());
		}
	}

	@Override
	public Result<Integer> update(SqlBuilder sqlBuilder) {
		try {
			int update = tongjiRepository.update(sqlBuilder.update());
			return Result.newSuccess(update);
		}catch(Exception e) {
			return Result.newFailure(1, e.getMessage());
		}
	}

	@Override
	public Result<Integer> delete(SqlBuilder sqlBuilder) {
		try {
			int update = tongjiRepository.update(sqlBuilder.delete());
			return Result.newSuccess(update);
		}catch(Exception e) {
			return Result.newFailure(1, e.getMessage());
		}
	}

	public <T> Result<List<T>> list(SqlBuilder sqlBuilder, RowMapper<T> rowMapper) {
		Result<List> list = list(sqlBuilder);
		if(list.hasObject()) {
			try {
				List<T> items = new ArrayList<>();
				for(Object obj : list.getObject()) {
					Object[] arr = (Object[])obj;
					T item = rowMapper.mapRow(arr);
					items.add(item);
				}
				return Result.newSuccess(items);
			}catch (Exception e) {
				return Result.newException(e);
			}
		}else return Result.newFailure(list);
	}
}
