package com.example.resistance.controller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.resistance.entity.ChatLog;
import com.example.resistance.entity.Member;
import com.example.resistance.entity.Room;
import com.example.resistance.properties.AppProperties;
import com.example.resistance.service.ChatService;
import com.example.resistance.service.RoomService;
import com.example.resistance.session.UserSession;

@Mapper
@Controller
//@Async
public class ChatController {

		@Autowired
		private RoomService roomService;
		@Autowired
		private ChatService chatService;
	    @Autowired
	    public UserSession userSession;
	    @Autowired
	    public AppProperties appProperties;

	    /**
	     * チャットログを画面に表示
	     */
	    @MessageMapping("/chat")
	    @SendTo("/receive/chat")
	    public ChatLog send(ChatLog chatLog) throws Exception {
	        Thread.sleep(100);
	        ChatLog log = new ChatLog();

	        // 発言内容追加
	        log.setText(chatLog.getText());

			// メンバーID追加
	        log.setMemberId(chatLog.getMemberId());

			// ルームID追加
	        log.setRoomId(chatLog.getRoomId());

			// 発言者を追加
	        log.setTalker(chatLog.getTalker());

			// チャットログをテーブルに登録
			chatService.registChatLog(chatLog);

	        return  log;
	    }

	    /**
	     * チャットルームページ表示
	     */
		@RequestMapping(value = "/chat", method = RequestMethod.GET)
		public String chat(@ModelAttribute Room room,@ModelAttribute Member member, @ModelAttribute ChatLog chatLog, Model model, BindingResult result) {

			// ページ情報取得
			getPageInfo(model, room);

			return "chatroom";
		}

	    /**
	     *
	     */
		@RequestMapping(value = "/chat_a", method = RequestMethod.GET)
		@ResponseBody
		public List<ChatLog> chat_a(@ModelAttribute Room room,@ModelAttribute Member member, @ModelAttribute ChatLog chatLog, Model model, BindingResult result) {

			// 部屋のチャットログを取得
			List<ChatLog> chatLogs = getChatLog();

			return chatLogs;
		}

		/*
		 * 入室しているメンバーを表示
		 */
		@RequestMapping(value = "/gameroom", method = RequestMethod.POST)
		public String create(@ModelAttribute Room room, Model model, BindingResult result) {
			model.addAttribute("room", room);

			List<Member> players = roomService.showMember(room);
			model.addAttribute("players",players);

			model.addAttribute("userName", userSession.getUserName());

			return "chatroom";
		}

		/**
		 * 退室します。
		 * @param room
		 * @param member
		 * @param model
		 * @param result
		 * @return
		 */
		@RequestMapping(value = "/exit_chat_top", method = RequestMethod.GET)
		public String exit(@ModelAttribute Room room,@ModelAttribute Member member, Model model, BindingResult result) {

			// roomテーブルからルーム情報を取得
			List<Room> rooms = roomService.showRooms();

			// セッション情報から入室しているメンバー情報を取得
			Member roomMember = userSession.getMember();

			// メンバー情報を削除
			roomService.exitRoom(roomMember);

			model.addAttribute("rooms", rooms);

			return "chat_top";
		}

		/**
		 * ページ情報取得メソッド
		 * @param model
		 * @param room
		 * @return
		 */
		public Model getPageInfo(Model model, Room room) {

				room = userSession.getRoom();

				Member member = userSession.getMember();

				// 参加者名を取得
				String memberName = userSession.getUserName();

			// Playerテーブルからroom_idに該当するプレイヤー情報を取得
			List<Member> members = roomService.showMember(room);

			// 検索用のChatLog
			ChatLog chatLog = new ChatLog();
			chatLog.setRoomId(room.getId());

			// 部屋のチャットログを取得
			List<ChatLog> chatLogs = chatService.showChatLogs(chatLog);

			// ページに渡す情報
			model.addAttribute("room", room);
			model.addAttribute("memberName", memberName);
			model.addAttribute("memberId", member.getId());
			model.addAttribute("entMember", member);
			model.addAttribute("members", members);
			model.addAttribute("userName", userSession.getUserName());
			model.addAttribute("messages",chatLogs);

			return model;
		}

		/*
		 * チャットログ取得メソッド
		 */
		private List<ChatLog> getChatLog() {

			// 「入室」押下時に保存したセッション情報(room)取得
			Room room = userSession.getRoom();

			// 検索用のChatLog
			ChatLog chatLog = new ChatLog();
			chatLog.setRoomId(room.getId());

			// 部屋のチャットログを取得
			List<ChatLog> chatLogs = chatService.showChatLogs(chatLog);

			return chatLogs;
		}
}
