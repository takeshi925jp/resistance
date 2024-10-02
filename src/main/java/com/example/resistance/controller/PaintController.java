package com.example.resistance.controller;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.resistance.entity.ChatLog;
import com.example.resistance.entity.Member;
import com.example.resistance.entity.Room;
import com.example.resistance.form.Canvas;
import com.example.resistance.form.GalleryForm;
import com.example.resistance.form.GalleryRequest;
import com.example.resistance.form.RequestForm;
import com.example.resistance.properties.AppProperties;
import com.example.resistance.service.GalleryService;
import com.example.resistance.service.RoomService;
import com.example.resistance.session.UserSession;

@Mapper
@Controller
public class PaintController {

	 @Autowired
	 private RoomService roomService;
     @Autowired
     public UserSession userSession;
     @Autowired
     public GalleryService galleryService;
     @Autowired
     public AppProperties prop;

	@RequestMapping(value = "/paint", method = RequestMethod.GET)
	public String paint(@ModelAttribute Room room,@ModelAttribute Member member, @ModelAttribute ChatLog chatLog, Model model, BindingResult result) {

		// ページ情報取得
		getPageInfo(model, room);

		// ---チェック用
//		// プロパティからギャラリー格納パスを取得
        String corrent = new File(".").getAbsoluteFile().getParent();
        corrent = corrent + "\\images\\";
//        corrent = corrent + "\\src\\main\\resources\\static\\image\\";

		model.addAttribute("localPath", corrent);



		return "paintroom";
	}

	/**
	 * canvasの描画内容を受け取る
	 */
	@RequestMapping(value = "/paint_save", method = RequestMethod.POST)
	@ResponseBody
	public RequestForm paint_save(@RequestBody GalleryRequest requestData,@ModelAttribute Room room,@ModelAttribute Member member, @ModelAttribute ChatLog chatLog, Model model, BindingResult result) throws Exception {

		Charset charset = StandardCharsets.UTF_8;

		String data = requestData.getData();

		// base64をバイナリ形式に変換
		int index = data.indexOf(",");
		String paintDataBase64 = data.substring(index + 1, data.length());

        byte[]paintDataEnc = Base64.getMimeDecoder().decode(paintDataBase64);

//		// PNGとして保存（画像データのパスを取得）
//		try {
//
//			// 保存するファイル名
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//			String fileName = LocalDateTime.now().format(dtf);
//
////			// プロパティからギャラリー格納パスを取得
//	        String corrent = new File(".").getAbsoluteFile().getParent();
//	        corrent = "\\images\\";
//	        //corrent = corrent + "\\images\\";
//
//			model.addAttribute("localPath", corrent);
//
//	        // 描画の保存先のフォルダパス
//	        Path folderPath = Paths.get(corrent);
//	        // 描画の保存先のファイルパス
//	        Path filePath = Paths.get(corrent + fileName + prop.getPaintExtension());
//
//	        File saveFile = new File("");
//
//
//			saveFile = filePath.toFile();
//			// 描画を保存
////			if (Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS)) {
////				saveFile = filePath.toFile();
////			} else {
////				RequestForm request = new RequestForm();
////				return request;
////			}
//
//			// バイナリデータから画像の取得
//			InputStream is = new ByteArrayInputStream(paintDataEnc);
//			BufferedImage image = ImageIO.read(is);
//			ImageIO.write(image, "PNG", saveFile);

			// DBに画像パス、作者、作品名を登録
			GalleryForm galleryForm = new GalleryForm();
			galleryForm.setCreator(userSession.getUserName());
			galleryForm.setGallery_name(requestData.getGalleryName());
			galleryForm.setPicture_data(data);
//			galleryForm.setGallery_path("/images/" + filePath.getFileName());
			//galleryForm.setGallery_path("/image/" + path.getFileName());

			galleryService.registGallery(galleryForm);

//		} catch (Exception e) {
//			// 保存に失敗したメッセージを設定
//			e.printStackTrace();
//			throw new Exception(e);
//		}

		RequestForm request = new RequestForm();
		request.setText(data);

		return request;
	}

	/**
	 * ページ情報取得メソッド
	 */
	private Model getPageInfo(Model model, Room room) {

		// 「入室」押下時に保存したセッション情報(room)取得
		room = userSession.getRoom();

		// 参加者名を取得
		String memberName = userSession.getUserName();

		// Playerテーブルからroom_idに該当するプレイヤー情報を取得
		List<Member> members = roomService.showMember(room);

		// 検索用のChatLog
		ChatLog chatLog = new ChatLog();
		chatLog.setRoomId(room.getId());

		// ページに渡す情報
		model.addAttribute("room", room);
		model.addAttribute("memberName", memberName);
		model.addAttribute("members", members);
		model.addAttribute("userName", userSession.getUserName());

		return model;
	}

	/**
	 * 描画を受け取って画面に反映
	 * @param galleryForm
	 * @return
	 * @throws Exception
	 */
    @MessageMapping("/paint")
    @SendTo("/receive/paint")
    public Canvas send(Canvas canvas) throws Exception {
        Thread.sleep(100);

        return canvas;
    }

	/**
	 * 退室します。
	 * @param room
	 * @param member
	 * @param model
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/exit_paint_top", method = RequestMethod.POST)
	public String exit(@ModelAttribute Room room,@ModelAttribute Member member, Model model, BindingResult result) {

		// セッション情報から入室しているメンバー情報を取得
		Member roomMember = userSession.getMember();

		// メンバー情報を削除
		roomService.exitRoom(roomMember);

		return "redirect:paint_top";
	}
}
