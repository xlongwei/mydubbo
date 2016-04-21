package com.xlongwei.archetypes.dubbo.facade;

import com.xlongwei.archetypes.dubbo.util.Result;

public interface IdService {
	Result<Long> next();
}
