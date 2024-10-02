package com.example.resistance.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resistance.entity.ChatLog;
import com.example.resistance.entity.Member;
import com.example.resistance.mapper.ChatMapper;

@Mapper
@Service
public class ChatService {

	@Autowired
	private ChatMapper chatMapper;

	//
	public void registPosition(Member member) {

		// 役職を登録
		chatMapper.registPosition(member);
	}

	// チャットログを登録
	public void registChatLog(ChatLog chatLog) {

		// チャットログを登録
		chatMapper.registChatLog(chatLog);
	}

	// チャットログを取得
	public List<ChatLog> showChatLogs(ChatLog chatLog) {

		// チャットログを登録
		List<ChatLog> chatLogs = chatMapper.showChatLogs(chatLog);

		return chatLogs;
	}
}
