package com.xlongwei.archetypes.dubbo.facade;

import java.util.List;

import com.xlongwei.archetypes.dubbo.util.PageObject;
import com.xlongwei.archetypes.dubbo.util.Pager;
import com.xlongwei.archetypes.dubbo.util.Result;
import com.xlongwei.archetypes.dubbo.util.SpecParam;
import com.xlongwei.archetypes.dubbo.util.SqlBuilder;

public interface BaseService<Entity> {
	Result<Entity> create(Entity entity);//添加记录
	Result<Entity> retrieve(Long entityId);//获取记录
	Result<Entity> update(Entity entity);//更新记录
	Result<Entity> delete(Long entityId);//删除记录，仅标记
	Result<PageObject<Entity>> page(SpecParam<Entity> specs, Pager pager);//分页查询
	Result<List<Entity>> list(SpecParam<Entity> specs);//全部记录
	Result<Integer> sqlCount(SqlBuilder sqlBuilder);//sql计数
	Result<List<Entity>> sqlPage(SqlBuilder sqlBuilder);//sql分页，必须是select id
}
