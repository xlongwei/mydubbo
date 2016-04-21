package com.xlongwei.archetypes.dubbo.repository;

import com.xlongwei.archetypes.dubbo.entity.User;


public interface UserRepository extends BaseRepository<User> {
	User findByUserName(String userName);
}
