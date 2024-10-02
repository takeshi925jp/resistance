package com.example.resistance.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLog {

	private Integer id;
	private Integer memberId;
	private Integer roomId;
	private String talker;
	private String text;

	public ChatLog() {}

	public ChatLog(Integer id, Integer memberId, Integer roomId, String talker, String text) {
		super();

		this.id = id;
		this.memberId = memberId;
		this.roomId = roomId;
		this.talker= talker;
		this.text= text;
	}}
