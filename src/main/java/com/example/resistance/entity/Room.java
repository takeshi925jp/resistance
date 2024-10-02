package com.example.resistance.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Room implements Serializable {

	private Integer id;
	@Pattern(regexp = "^[0-9]{3}$", message = "99人以下の数値で入力してください。")
	private Integer size;
	private Integer currentMemberNum;
	private String name;
	private String description;
	private String userSession;
	private LocalDateTime dateTime;
	private String password;
	private Integer roomKind;

	public Room( Integer id, Integer size, Integer currentMemberNum, String name, String description, String password, LocalDateTime dateTime, String userSession, Integer roomKind) {
		super();

		this.id = id;
		this.size = size;
		this.currentMemberNum = currentMemberNum;
		this.name = name;
		this.description = description;
		this.userSession = userSession;
		this.dateTime = dateTime;
		this.password = password;
		this.roomKind = roomKind;

	}
}
