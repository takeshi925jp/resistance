package com.example.resistance.controller;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.resistance.entity.Room;

@Mapper
@Controller
public class TopController {


	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public String top(@ModelAttribute("modelMap")ModelMap modelMap, @ModelAttribute Room room, Model model, BindingResult result) {

		return "top";
	}
}
