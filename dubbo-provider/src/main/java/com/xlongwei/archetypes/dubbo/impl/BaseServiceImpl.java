package com.xlongwei.archetypes.dubbo.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.xlongwei.archetypes.dubbo.entity.BaseEntity;
import com.xlongwei.archetypes.dubbo.facade.BaseService;
import com.xlongwei.archetypes.dubbo.facade.IdService;
import com.xlongwei.archetypes.dubbo.facade.TongjiService;
import com.xlongwei.archetypes.dubbo.repository.BaseRepository;
import com.xlongwei.archetypes.dubbo.util.GlobalConfig;
import com.xlongwei.archetypes.dubbo.util.NumberUtil;
import com.xlongwei.archetypes.dubbo.util.PageObject;
import com.xlongwei.archetypes.dubbo.util.Pager;
import com.xlongwei.archetypes.dubbo.util.PagerUtil;
import com.xlongwei.archetypes.dubbo.util.Result;
import com.xlongwei.archetypes.dubbo.util.SpecParam;
import com.xlongwei.archetypes.dubbo.util.SpecUtil;
import com.xlongwei.archetypes.dubbo.util.SqlBuilder;

public abstract class BaseServiceImpl<Entity extends BaseEntity> implements BaseService<Entity>, InitializingBean {
	@Autowired IdService idService;
	@Autowired TongjiService tongjiService;
	@Autowired CacheManager cacheManager;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected Class<Entity> entityClass;
	protected String entityCache,  listCache;

	public abstract BaseRepository<Entity> getRepository();
	
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		entityClass = (Class<Entity>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseService.class);
		entityCache = entityClass.getSimpleName()+".entity";
		listCache = entityClass.getSimpleName()+".list";
	}

	@Transactional
	public Result<Entity> create(Entity entity) {
		if(NumberUtil.validId(entity.getId())) return Result.newFailure(-1, "bad id: "+entity.getId());
		Result<Long> nextID = idService.next();
		if(!nextID.hasObject()) return Result.newFailure(nextID);
		entity.setId(nextID.getObject());
		Entity save = getRepository().save(entity);
		if(NumberUtil.parseBoolean(GlobalConfig.getProperty("base.cached"), true)) {
		cacheManager.getCache(listCache).clear();
		}
		return Result.newSuccess(save);
	}

	@Transactional
	public Result<Entity> update(Entity entity) {
		if(NumberUtil.validId(entity.getId())) {
			Entity save = getRepository().save(entity);
			if(NumberUtil.parseBoolean(GlobalConfig.getProperty("base.cached"), true)) {
			cacheManager.getCache(entityCache).evict(entity.getId());
			cacheManager.getCache(listCache).clear();
			}
			return Result.newSuccess(save);
		}
		return Result.newFailure(-1, "bad id: "+entity.getId());
	}

	@Transactional(readOnly=true)
	public Result<Entity> retrieve(Long entityId) {
		if(NumberUtil.validId(entityId)) {
			Entity entity = getFromCache(entityCache, String.valueOf(entityId), entityClass);
			if(entity == null) {
				entity = getRepository().findOne(entityId);
				if(entity!=null && NumberUtil.parseBoolean(GlobalConfig.getProperty("base.cached"), true)) cacheManager.getCache(entityCache).put(String.valueOf(entityId), entity);
			}
			if(entity!=null) return Result.newSuccess(entity);
		}
		return Result.newFailure(-1, "bad id: "+entityId);
	}

	@Transactional
	public Result<Entity> delete(Long entityId) {
		if(NumberUtil.validId(entityId)) {
			Entity entity = getRepository().findOne(entityId);
			if(entity!=null) {
				entity.setDeleted(true);
				Entity save = getRepository().save(entity);
				if(NumberUtil.parseBoolean(GlobalConfig.getProperty("base.cached"), true)) {
				cacheManager.getCache(entityCache).evict(entity.getId());
				cacheManager.getCache(listCache).clear();
				}
				return Result.newSuccess(save);
			}
		}
		return Result.newFailure(-1, "bad id: "+entityId);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Result<PageObject<Entity>> page(SpecParam<Entity> specs, Pager pager) {
		String key = String.valueOf(specs)+PagerUtil.limit(pager);
		Page<Entity> findAll = getFromCache(listCache, key, Page.class);
		if(findAll==null) {
			findAll = getRepository().findAll(SpecUtil.spec(specs), PagerUtil.pagable(pager));
			if(NumberUtil.parseBoolean(GlobalConfig.getProperty("base.cached"), true)) {
			cacheManager.getCache(listCache).put(key, findAll);
			}
		}
		return Result.newSuccess(PagerUtil.pageObject(findAll));
	}

	@Transactional(readOnly=true)
	public Result<Integer> sqlCount(SqlBuilder sqlBuilder) {
		sqlBuilder.select("count(id)");
		Result<Integer> count = tongjiService.count(sqlBuilder);
		return count;
	}

	@Transactional(readOnly=true)
	@SuppressWarnings("rawtypes")
	public Result<List<Entity>> sqlPage(SqlBuilder sqlBuilder) {
		sqlBuilder.select("id");
		List<Entity> entitys = new ArrayList<>();
		Result<List> list = tongjiService.list(sqlBuilder);
		if(list.hasObject()) {
			for(Object obj : list.getObject()) {
				Long id = NumberUtil.parseLong(String.valueOf(obj), null);
				if(!NumberUtil.validId(id)) continue;
				
				Result<Entity> retrieve = this.retrieve(id);
				if(retrieve.hasObject()) entitys.add(retrieve.getObject());
			}
		}
		return Result.newSuccess(entitys);
	}

	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Result<List<Entity>> list(SpecParam<Entity> specs) {
		String key = String.valueOf(specs);
		List<Entity> findAll = getFromCache(listCache, key, List.class);
		if(findAll==null) {
			findAll = getRepository().findAll(SpecUtil.spec(specs));
			if(NumberUtil.parseBoolean(GlobalConfig.getProperty("base.cached"), true)) {
			cacheManager.getCache(listCache).put(key, findAll);
			}
		}
		return Result.newSuccess(findAll);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getFromCache(String name, String key, Class<T> clazz) {
		if(NumberUtil.parseBoolean(GlobalConfig.getProperty("base.cached"), true)) {
		ValueWrapper valueWrapper = cacheManager.getCache(name).get(key);
		if(valueWrapper!=null) {
			Object value = valueWrapper.get();
			if(value!=null && clazz.isAssignableFrom(value.getClass())) {
				logger.info("get from cache: "+name+","+key+","+value);
				return (T)value;
			}
		}
		}
		return null;
	}
}
