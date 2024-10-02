package com.example.resistance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface RegistMapper {
	void registUser(@Param("loginId") String loginId,@Param("userName") String userName, @Param("password") String password);
}