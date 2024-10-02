package com.example.resistance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.resistance.entity.Room;

@Controller
public class TmpController {

	@RequestMapping(value = "/temp", method = RequestMethod.GET)
	public String show(@ModelAttribute("modelMap")ModelMap modelMap, @ModelAttribute("validationMessageMap")ModelMap validationMessageMap, @ModelAttribute Room room, Model model, BindingResult result) {



		return "temp";
	}

    @PostMapping("/test")
    @ResponseBody
    public String note(@RequestParam String note) {
        return note + note;
    }
}
