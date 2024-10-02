package com.example.resistance.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

	private Integer id;
	private Integer roomId;
	private String name;

	public Member( Integer id, Integer roomId, String name) {
		super();

		this.id = id;
		this.roomId = roomId;
		this.name= name;
	}
}
