package com.example.resistance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.resistance.entity.User;


@Mapper
public interface UserMapper {
	User getUser(@Param("loginId") String loginId, @Param("password") String password);
}
