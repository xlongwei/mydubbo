package com.xlongwei.archetypes.dubbo.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.xlongwei.archetypes.dubbo.repository.TongjiRepository;

@Repository("tongjiRepository")
public class TongjiReposityImpl implements TongjiRepository {
	@PersistenceContext private EntityManager entityManager;
	@Autowired private PlatformTransactionManager transactionManager;

	public int count(String sql) {
		Query query = entityManager.createNativeQuery(sql);
		return ((Number)query.getSingleResult()).intValue();
	}

	public Object[] sums(String sql) {
		List<?> list = list(sql);
		if(list!=null && list.size()>0) {
			Object obj = list.get(0);
			if(obj instanceof Object[]) {
				return (Object[])obj;
			}else {
				return new Object[] {obj};
			}
		}
		return null;
	}

	public List<?> list(String sql) {
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	@Transactional
	public int update(final String sql) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		Integer update = transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				Query query = entityManager.createNativeQuery(sql);
				return query.executeUpdate();
			}
		});
		return update;
	}
}
