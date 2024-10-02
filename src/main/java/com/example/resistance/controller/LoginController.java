package com.example.resistance.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.resistance.dto.UserDto;
import com.example.resistance.form.LoginForm;
import com.example.resistance.service.LoginService;

@Mapper
@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Autowired
	private HttpSession session;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@ModelAttribute LoginForm loginForm, Model model, BindingResult result) {
		model.addAttribute("loginForm", loginForm);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute @Valid LoginForm loginForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("loginForm", loginForm);
			return "login";
		}

		// ユーザー検索
		UserDto user = loginService.getUser(loginForm.getLoginId(), loginForm.getPassword());

		// ユーザーが存在しない場合
		if (user == null){
			result.rejectValue("loginId", "ログインに失敗しました", "ログインに失敗しました");
			return "login";
		}

		session.setAttribute("loginUser", user);

		return "redirect:top";
	}
}