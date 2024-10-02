package com.example.resistance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.resistance.entity.ChatLog;
import com.example.resistance.entity.Member;


@Mapper
public interface ChatMapper {

	// room情報取得
	public void registPosition(Member player);

	// チャットログ登録
	public void registChatLog(ChatLog chatLog);

	// チャットログを取得
	public List<ChatLog> showChatLogs(ChatLog chatLog);

}
