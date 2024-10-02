package com.example.resistance.service;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resistance.dto.UserDto;
import com.example.resistance.dto.factory.UserDtoFactory;
import com.example.resistance.mapper.UserMapper;

@Mapper
@Service
public class LoginService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserDtoFactory userDtoFactory;

	public UserDto getUser(String loginId, String password) {
		//return userDtoFactory.create(userMapper.getUser(loginId, CipherUtil.encrypt(password)));
		return userDtoFactory.create(userMapper.getUser(loginId, password));
	}

}