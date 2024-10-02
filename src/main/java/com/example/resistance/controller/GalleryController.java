package com.example.resistance.controller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.resistance.entity.Room;
import com.example.resistance.form.GalleryForm;
import com.example.resistance.service.GalleryService;
import com.example.resistance.session.UserSession;

@Mapper
@Controller
public class GalleryController {

    @Autowired
    public UserSession userSession;
    @Autowired
    public GalleryService galleryService;

	@RequestMapping(value = "/gallery", method = RequestMethod.GET)
	public String gallery(@ModelAttribute Room room,@ModelAttribute GalleryForm galleryForm, Model model, BindingResult result) {

			// ギャラリーの情報を取得
			List<GalleryForm> gallerys = galleryService.getGallery();

			// 画面側に送るデータ
			model.addAttribute("gallerys", gallerys);
		return "gallery";
	}

	/*
	 * ギャラリーのパスリストを取得する
	 */
//	private List<String> getGalleryPathList() {
//
//		// 返却するギャラリーパスリスト
//		List<String> galleryPathList = new ArrayList<String>();
//
//		// 画像のパスを取得
//		// カレントディレクトリに画像を保存
//		String corrent = new File(".").getAbsoluteFile().getParent();
//		corrent = corrent + "\\src\\main\\resources\\static\\image\\";
//		Path galleryPath = Paths.get(corrent);
//
//		try {
//			// 画像群の相対パス取得
//			galleryPathList = Files.list(galleryPath).map(p -> "./image/" + p.getFileName().toString())
//					.collect(Collectors.toList());
//
//		} catch (IOException e) {
//
//		}
//
//		return galleryPathList;
//	}
}
