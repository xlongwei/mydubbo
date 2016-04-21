package com.xlongwei.archetypes.dubbo.facade;

import com.xlongwei.archetypes.dubbo.entity.User;
import com.xlongwei.archetypes.dubbo.util.Result;

public interface UserService extends BaseService<User> {
	Result<User> getUser(String userName);
	Result<User> registerUser(User user);
	Result<Boolean> checkUserName(String userName);
}
