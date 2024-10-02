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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;

import com.example.resistance.common.constant.CommonConst;
import com.example.resistance.common.utils.CipherUtil;
import com.example.resistance.entity.Member;
import com.example.resistance.entity.Room;
import com.example.resistance.properties.AppProperties;
import com.example.resistance.service.RoomService;
import com.example.resistance.session.UserSession;

@Mapper
@Controller
public class PaintRoomController extends RoomController {
	@Autowired
	private RoomService roomService;
	@Autowired
	public UserSession userSession;
	@Autowired
	public AppProperties appProperties;

	@RequestMapping(value = "/paint_top", method = RequestMethod.GET)
	public String paintShow(@ModelAttribute("modelMap")ModelMap modelMap, @ModelAttribute Room room, Model model, BindingResult result) {

		// roomテーブルからルーム情報を取得
		List<Room> rooms = roomService.showPaintRooms();

		// メッセージマップを取得
		HashMap<String, String> messageMap = (HashMap<String, String>)modelMap.get("messageMap");

		// メッセージ情報を取得
		Optional<HashMap<String, String>> op = Optional.ofNullable(messageMap);

		// パス入力ミスメッセージ、該当するルームIDを取得
		Optional<String> passMessage = op.map(m -> m.get(CommonConst.PASS_MESSAGE));
		Optional<Integer> roomId = op.map(m -> Integer.parseInt(m.get(CommonConst.ROOM_ID)));

		// modelMapからメッセージを取得　"パスワードが一致していません"
		model.addAttribute(CommonConst.PASS_MESSAGE, passMessage.orElse(""));
		model.addAttribute(CommonConst.ROOM_ID, roomId.orElse(0));

		model.addAttribute("rooms", rooms);

		return "paint_top";
	}

	@RequestMapping(value = "/create_paint_room", method = RequestMethod.POST)
	public String create(@ModelAttribute("modelMap")ModelMap modelMap, RedirectAttributes redirectAttributes, @ModelAttribute Room room, Model model, BindingResult result, UriComponentsBuilder builder) {

		java.net.URI location = builder.path("/paint_top").build().toUri();
		String toRedirect = "redirect:" + location.toString();

		// メッセージマップを取得
		HashMap<String, String> messageMap = (HashMap<String, String>)modelMap.get(CommonConst.PASS_MESSAGE);

		// メッセージ情報を取得
		Optional<HashMap<String, String>> op = Optional.ofNullable(messageMap);

		// パス入力ミスメッセージ、該当するルームIDを取得
		Optional<String> passMessage = op.map(m -> m.get(CommonConst.PASS_MESSAGE));
		Optional<Integer> roomId = op.map(m -> Integer.parseInt(m.get(CommonConst.ROOM_ID)));

		// ルーム種別に「1」を設定（1：チャットルーム）
		room.setRoomKind(appProperties.getKindPaintRoom());
		// 初期値設定
		room.setCurrentMemberNum(0);
		// ルーム情報
		model.addAttribute("room", room);

//		// 部屋作成時のバリデーションチェック
//		ModelMap validationMessageMap = createRoomValidation(room, result);
//
//		if (validationMessageMap.size() > 0) {
//			redirectAttributes.addFlashAttribute("validationMessageMap", validationMessageMap);
//			return toRedirect;
//		}

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
		List<Room> rooms = roomService.showPaintRooms();

		// modelMapからメッセージを取得　"パスワードが一致していません"
		model.addAttribute(CommonConst.PASS_MESSAGE, passMessage.orElse(""));
		model.addAttribute(CommonConst.ROOM_ID, roomId.orElse(0));
		model.addAttribute("validationMessageList", validationMessageList);
		model.addAttribute("rooms", rooms);

		return "paint_top";
	}

	// 「入室」押下時
	@RequestMapping(value = "/enter_paintroom", method = RequestMethod.POST)
	public String roomPlayer(RedirectAttributes redirectAttributes, @ModelAttribute Member member,@ModelAttribute Room room, Model model, BindingResult result, UriComponentsBuilder builder) {

//		UriComponentsBuilder builder2 = builder.cloneBuilder();
//
//		java.net.URI locationTop = builder.path("/paint_top").build().toUri();
//		java.net.URI location = builder2.path("/paint").build().toUri();
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
			return "paint_top";
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

		return "paintroom";
	}

	@RequestMapping(value = "/delete_paintroom", method = RequestMethod.POST)
	public String deleteRoom(@ModelAttribute Room room, Model model, BindingResult result, UriComponentsBuilder builder) {
		model.addAttribute("room", room);

//		java.net.URI location = builder.path("/paint_top").build().toUri();
//		String toRedirect = "redirect:" + location.toString();

		roomService.deleteRoom(room);

		// roomテーブルからルーム情報を取得
		List<Room> rooms = roomService.showPaintRooms();

		model.addAttribute("rooms", rooms);

		return "paint_top";
	}

//	private ModelMap validation(Room room){
//
//		ModelMap modelMap = new ModelMap();
//		List<String> validationMessageList = new ArrayList<>();
//
//		// ルーム人数の超過チェック
//		if(room.getSize() <= room.getCurrentMemberNum()) {
//			// メッセージ
//			validationMessageList.add(appProperties.getMessageOverRoomSize());
//		}
//
//		// パスワードの正規表現チェック
//		String password = room.getPassword();
//		Pattern p = Pattern.compile(appProperties.getPasswordPattern());
//		Matcher m = p.matcher(password);
//		// パスワード入力失敗
//		if(!m.matches()) {
//			validationMessageList.add(appProperties.getMissingPasswordPattern());
//		}
//
//		// パスワード照会の結果を取得（modelMapに値がある場合は照会失敗）
//		boolean checkResult = checkPassword(room);
//
//		// パスワード照会に失敗した場合
//		if (!checkResult) {
//			validationMessageList.add(appProperties.getPassMissingMessage());
//		}
//
//		// モデルマップの作成
//		modelMap.addAttribute("validationMessageList", validationMessageList);
//
//
//		return modelMap;
//	}

	/**
	 * 入室押下時のバリデーション
	 */
	private ModelMap enterValidation(Room registedRoom, Room room) {

		ModelMap modelMap = new ModelMap();
		List<String> validationMessageList = new ArrayList<>();

		// ルーム人数の超過チェック
		if(registedRoom.getSize() <= registedRoom.getCurrentMemberNum()) {
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
}
