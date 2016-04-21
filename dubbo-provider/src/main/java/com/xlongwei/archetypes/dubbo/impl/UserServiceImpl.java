package com.xlongwei.archetypes.dubbo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xlongwei.archetypes.dubbo.entity.User;
import com.xlongwei.archetypes.dubbo.facade.IdService;
import com.xlongwei.archetypes.dubbo.facade.UserService;
import com.xlongwei.archetypes.dubbo.repository.BaseRepository;
import com.xlongwei.archetypes.dubbo.repository.UserRepository;
import com.xlongwei.archetypes.dubbo.util.Result;
import com.xlongwei.archetypes.dubbo.util.StringUtil;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	@Autowired private UserRepository userRepository;
	@Autowired private IdService idService;
	
	public BaseRepository<User> getRepository() {
		return userRepository;
	}

	@Transactional(readOnly=true)
	public Result<User> getUser(String userName) {
		if(!StringUtil.isBlank(userName)) {
			User user = userRepository.findByUserName(userName);
			if(user!=null) return Result.newSuccess(user);
		}
		return Result.newFailure(-1, "bad userName: "+userName);
	}

	@Transactional
	public Result<User> registerUser(User user) {
		if(user.getId()!=null) return Result.newFailure(-1, "register user with id: "+user.getId());
		Result<Long> nextID = idService.next();
		if(!nextID.hasObject()) return Result.newFailure(nextID);
		user.setId(nextID.getObject());
		User save = userRepository.save(user);
		return Result.newSuccess(save);
	}

	@Transactional(readOnly=true)
	public Result<Boolean> checkUserName(String userName) {
		if(!StringUtil.isBlank(userName)) {
			User user = userRepository.findByUserName(userName);
			if(user!=null) return Result.newSuccess(true);
		}
		return Result.newSuccess(false);
	}
}
