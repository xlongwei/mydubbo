package com.xlongwei.archetypes.dubbo.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.xlongwei.archetypes.dubbo.facade.IdService;
import com.xlongwei.archetypes.dubbo.util.DateUtil;
import com.xlongwei.archetypes.dubbo.util.DateUtil.FormatType;
import com.xlongwei.archetypes.dubbo.util.NumberUtil;
import com.xlongwei.archetypes.dubbo.util.Result;

@Service("idService")
public class IdServiceImpl implements IdService {
	private Long last = Long.MIN_VALUE;
	
	@Override
	public Result<Long> next() {
		String format = DateUtil.format(new Date(), FormatType.MICROSECONDS);
		Long parseLong = NumberUtil.parseLong(format, last);
		if(parseLong>last) {
			last = parseLong;
			return Result.newSuccess(parseLong);
		}else {
			last++;
			return Result.newSuccess(last);
		}
	}

}
