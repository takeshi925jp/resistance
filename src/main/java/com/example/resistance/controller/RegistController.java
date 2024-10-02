package com.example.resistance.controller;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.resistance.common.utils.CipherUtil;
import com.example.resistance.form.RegistForm;
import com.example.resistance.service.RegistService;

@Mapper
@Controller
public class RegistController {
	@Autowired
	private RegistService registService;

	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String regist(@ModelAttribute RegistForm registForm, Model model, BindingResult result) {
		model.addAttribute("registForm", registForm);
		return "regist";
	}

	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public String regist(@ModelAttribute @Valid RegistForm registForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("registForm", registForm);
			return "regist";
		}

		// ユーザー登録入力情報
		String loginId = registForm.getLoginId();
		String userName = registForm.getUserName();
		String password = registForm.getPassword();
		String rePassword = registForm.getRePassword();

		if (checkParam(result, loginId, userName, password, rePassword)) {

			// ユーザー新規登録
			registService.registUser(loginId, userName, CipherUtil.encrypt(password));

		} else {

			return "regist";
		}
		return "redirect:chat_top";
	}

	private boolean checkParam(BindingResult result, String loginId, String userName, String password,
			String rePassword) {

		// 返却値
		boolean checkResult = true;

		// パスワード入力確認
		if (!(password.equals(rePassword))) {

			// チェック判定 エラー
			checkResult = false;
			result.rejectValue("password", "パスワードが一致しません", "パスワードが一致しません");
		}

		// 入力チェック
		if (loginId.isBlank() || userName.isBlank() || password.isBlank() || rePassword.isBlank()) {

			// チェック判定 エラー
			checkResult = false;
			result.rejectValue("password", "未入力の項目があります", "未入力の項目があります");
		}

		return checkResult;
	}
}