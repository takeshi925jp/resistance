package com.example.resistance.entity;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private Integer id;
	private String loginId;
	private String name;
	private String password;
	private Date createdDate;

	public User(Integer id, String loginId, String name, String password, Date createdDate) {
		super();
		this.id = id;
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.createdDate = createdDate;
	}
}