package com.example.resistance.controller;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.resistance.common.utils.CipherUtil;
import com.example.resistance.entity.Room;
import com.example.resistance.properties.AppProperties;
import com.example.resistance.service.RoomService;

/**
 *部屋作成の基幹となるクラス
 * 共通メソッド等はここに定義
 *
*/
@Mapper
@Controller
public class RoomController extends ResponseEntityExceptionHandler  {
	@Autowired
	public AppProperties appProperties;
	@Autowired
	public RoomService roomService;

	/**
	 * 入力した部屋のパスワードをチェックする
	 */
	public boolean checkPassword(Room room) {

		boolean result = true;

		// 入力したパスワードを取得
		String password = room.getPassword();

		if (!StringUtils.isEmpty(password)) {
			password = CipherUtil.encrypt(password);
		}
		// 部屋のパスワードを取得
		Room registedRoom = roomService.showRoom(room);

		// PASS付きの部屋にのみ処理
		if (!registedRoom.getPassword().equals("")) {
			// パスワードの照会、一致しなかった場合
			if (!password.equals(registedRoom.getPassword())) {
				result = false;
			}
		}
		return result;
	}
}
