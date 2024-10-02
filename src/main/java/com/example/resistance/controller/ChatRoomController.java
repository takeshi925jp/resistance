package com.example.resistance.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;

import com.example.resistance.common.constant.CommonConst;
import com.example.resistance.common.utils.CipherUtil;
import com.example.resistance.entity.ChatLog;
import com.example.resistance.entity.Member;
import com.example.resistance.entity.Room;
import com.example.resistance.properties.AppProperties;
import com.example.resistance.service.ChatService;
import com.example.resistance.service.RoomService;
import com.example.resistance.session.UserSession;


@Mapper
@Controller
public class ChatRoomController extends RoomController {
	@Autowired
	private RoomService roomService;
	@Autowired
	public UserSession userSession;
	@Autowired
	public AppProperties appProperties;
	@Autowired
	private ChatService chatService;

	@RequestMapping(value = "/chat_top", method = RequestMethod.GET)
	public String show(@ModelAttribute("modelMap")ModelMap modelMap, @ModelAttribute("validationMessageMap")ModelMap validationMessageMap, @ModelAttribute Room room, Model model, BindingResult result) {

		// roomテーブルからルーム情報を取得
		List<Room> rooms = roomService.showRooms();

		// メッセージマップを取得
		HashMap<String, String> messageMap = (HashMap<String, String>)modelMap.get(CommonConst.PASS_MESSAGE);

		// メッセージ情報を取得
		Optional<HashMap<String, String>> op = Optional.ofNullable(messageMap);

		// バリデーションのメッセージを取得
		List<String> validationMessageList = (List<String>)validationMessageMap.get("validationMessageList");

		// パス入力ミスメッセージ、該当するルームIDを取得
		Optional<String> passMessage = op.map(m -> m.get(CommonConst.PASS_MESSAGE));
		Optional<Integer> roomId = op.map(m -> Integer.parseInt(m.get(CommonConst.ROOM_ID)));

		// modelMapからメッセージを取得　"パスワードが一致していません"
		model.addAttribute(CommonConst.PASS_MESSAGE, passMessage.orElse(""));
		model.addAttribute(CommonConst.ROOM_ID, roomId.orElse(0));
		model.addAttribute("validationMessageList", validationMessageList);
		model.addAttribute("rooms", rooms);

		return "chat_top";
	}

	/**
	 * 部屋を作成する
	 * @param room
	 * @param model
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/create_room", method = RequestMethod.GET)
	public String create(@ModelAttribute("modelMap")ModelMap modelMap, @ModelAttribute Room room, Model model, BindingResult result, UriComponentsBuilder builder) {

		// メッセージマップを取得
		HashMap<String, String> messageMap = (HashMap<String, String>)modelMap.get(CommonConst.PASS_MESSAGE);

		// メッセージ情報を取得
		Optional<HashMap<String, String>> op = Optional.ofNullable(messageMap);

		// パス入力ミスメッセージ、該当するルームIDを取得
		Optional<String> passMessage = op.map(m -> m.get(CommonConst.PASS_MESSAGE));
		Optional<Integer> roomId = op.map(m -> Integer.parseInt(m.get(CommonConst.ROOM_ID)));


		// ルーム種別に「0」を設定（0：チャットルーム）
		room.setRoomKind(appProperties.getKindChatRoom());
		// 初期値設定
		room.setCurrentMemberNum(0);

		// ルーム情報
		model.addAttribute("room", room);

		// 部屋作成時のバリデーションチェック
		ModelMap validationMessageMap = createRoomValidation(room, result);

		// バリデーションのメッセージを取得
		List<String> validationMessageList = (List<String>)validationMessageMap.get("validationMessageList");
		// パスワードを暗号化
		if (!(room.getPassword().equals(""))) {
			room.setPassword(CipherUtil.encrypt(room.getPassword()));
		}

		// ルーム作成
		roomService.createRoom(room);

		// roomテーブルからルーム情報を取得
		List<Room> rooms = roomService.showRooms();

		// modelMapからメッセージを取得　"パスワードが一致していません"
		model.addAttribute(CommonConst.PASS_MESSAGE, passMessage.orElse(""));
		model.addAttribute(CommonConst.ROOM_ID, roomId.orElse(0));
		model.addAttribute("validationMessageList", validationMessageList);
		model.addAttribute("rooms", rooms);

		return "chat_top";
	}

//	/**
//	 * 部屋を作成する
//	 * @param room
//	 * @param model
//	 * @param result
//	 * @return
//	 */
//	@RequestMapping(value = "/create_room", method = RequestMethod.GET)
//	public String create(@Validated RedirectAttributes redirectAttributes, @ModelAttribute Room room, Model model, BindingResult result, UriComponentsBuilder builder) {
//
//		java.net.URI location = builder.path("/chat_top").build().toUri();
//		String toRedirect = "redirect:" + location.toString();
//
//		// ルーム種別に「0」を設定（0：チャットルーム）
//		room.setRoomKind(appProperties.getKindChatRoom());
//		// 初期値設定
//		room.setCurrentMemberNum(0);
//
//		// ルーム情報
//		model.addAttribute("room", room);
//
//		// 部屋作成時のバリデーションチェック
//		ModelMap validationMessageMap = createRoomValidation(room, result);
//
//		if (validationMessageMap.size() > 0) {
//			redirectAttributes.addFlashAttribute("validationMessageMap", validationMessageMap);
//			return toRedirect;
//		}
//
//		// パスワードを暗号化
//		if (!(room.getPassword().equals(""))) {
//			room.setPassword(CipherUtil.encrypt(room.getPassword()));
//		}
//
//		// ルーム作成
//		roomService.createRoom(room);
//
//		return toRedirect;
//	}

	/**
	 * 「入室」押下時
	 * @param redirectAttributes
	 * @param member
	 * @param room
	 * @param model
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/enter_chatroom", method = RequestMethod.GET)
	public String roomPlayer(RedirectAttributes redirectAttributes, @ModelAttribute Member member,@ModelAttribute Room room, Model model, BindingResult result, UriComponentsBuilder builder) {

//		UriComponentsBuilder builder2 = builder.cloneBuilder();

//		java.net.URI locationTop = builder.path("/chat_top").build().toUri();
//		java.net.URI location = builder2.path("/chat").build().toUri();
//		String toRedirectTop = "redirect:" + locationTop.toString();
//		String toRedirect = "redirect:" + location.toString();

		// roomテーブルからルーム情報を取得
		List<Room> rooms = roomService.showRooms();

		// ルームのIDをセット
		room.setId(member.getRoomId());

		// ルーム情報取得
		Room registedRoom = roomService.showRoom(room);

		// 入室時のバリデーションチェック
		ModelMap validationMessageMap = enterValidation(registedRoom, room);

		if (validationMessageMap.size() > 0) {
			//redirectAttributes.addFlashAttribute("validationMessageMap", validationMessageMap);			// 登録はせずに画面に戻る
			// バリデーションのメッセージを取得
			List<String> validationMessageList = (List<String>)validationMessageMap.get("validationMessageList");
			model.addAttribute("validationMessageList", validationMessageList);
			model.addAttribute("rooms", rooms);
			return "chat_top";
		}

		// 参加者情報
		model.addAttribute("member", member);

		// 入室者のメンバー情報を登録
		roomService.createMember(member);

		// ルームのIDをセット
		room.setId(member.getRoomId());

		// 部屋のメンバー情報取得
		List<Member> members = roomService.showMember(room);
		// 部屋のメンバー情報をhtmlに渡す
		model.addAttribute("members", members);

		// ルームテーブルの参加者人数を更新
		roomService.updateMemberNum(room);
		room = roomService.showRoom(room);

		// 参加者情報
		model.addAttribute("room", room);

		// セッション情報を追加
		userSession.setMember(member);
		userSession.setUserName(member.getName());
		userSession.setRoom(room);

		getPageInfo(model, room);

		return "chatroom";
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

	@RequestMapping(value = "/delete_room", method = RequestMethod.GET)
	public String deleteRoom(@ModelAttribute Room room, Model model, BindingResult result, UriComponentsBuilder builder) {
		model.addAttribute("room", room);

//		java.net.URI location = builder.path("/chat_top").build().toUri();
//		String toRedirect = "redirect:" + location.toString();

		roomService.deleteRoom(room);

		// roomテーブルからルーム情報を取得
		List<Room> rooms = roomService.showRooms();

		model.addAttribute("rooms", rooms);

		return "chat_top";

	}

	/**
	 * 入室押下時のバリデーション
	 */
	private ModelMap enterValidation(Room registredRoom, Room room){

		ModelMap modelMap = new ModelMap();
		List<String> validationMessageList = new ArrayList<>();

		// ルーム人数の超過チェック
		if(registredRoom.getSize() <= registredRoom.getCurrentMemberNum()) {
			// メッセージ
			validationMessageList.add(appProperties.getMessageOverRoomSize());
		}

		// パスワードがある場合はチェック
		if (!StringUtils.isEmpty(room.getPassword())) {

			// パスワードの正規表現チェック
			String password = room.getPassword();
			Pattern p = Pattern.compile(appProperties.getPasswordPattern());
			Matcher m = p.matcher(password);
			// パスワード入力パターン不正
			if (!m.matches()) {
				validationMessageList.add(appProperties.getMissingPasswordPattern());
			}

			// パスワード照会の結果を取得（modelMapに値がある場合は照会失敗）
			boolean checkResult = checkPassword(room);

			// パスワード照会に失敗した場合
			if (!checkResult) {
				validationMessageList.add(appProperties.getPassMissingMessage());
			}
		}

		if (validationMessageList.size() > 0) {
			// モデルマップの作成
			modelMap.addAttribute("validationMessageList", validationMessageList);
		}

		return modelMap;
	}

	/**
	 * 部屋作成時のバリデーション
	 */
	private ModelMap createRoomValidation(Room room, BindingResult result){

		ModelMap modelMap = new ModelMap();
		List<String> validationMessageList = new ArrayList<>();

		// BeanValidation
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
            	validationMessageList.add(error.getDefaultMessage());
            }
        }

		// 参加可能人数のフォーマットチェック
		if (room.getSize() != null) {
			Pattern pattern = Pattern.compile("^[0-9]{1,2}");
			if (!pattern.matcher(room.getSize().toString()).matches()) {
				validationMessageList.add(appProperties.getNumberFormatError());
			}
		}

		// パスワードがある場合はチェック
		if (!StringUtils.isEmpty(room.getPassword())) {

			// パスワードの正規表現チェック
			String password = room.getPassword();
			Pattern p = Pattern.compile(appProperties.getPasswordPattern());
			Matcher m = p.matcher(password);
			// パスワード入力パターン不正
			if (!m.matches()) {
				validationMessageList.add(appProperties.getMissingPasswordPattern());
			}
		}

		if (validationMessageList.size() > 0) {
			// モデルマップの作成
			modelMap.addAttribute("validationMessageList", validationMessageList);
		}

		return modelMap;
	}

    @ExceptionHandler(Exception.class)
    public String occurException(Exception exception){
    	System.out.println("bindEx");
        return "forward:chat_top";
    }

}