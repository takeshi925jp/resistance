package com.example.resistance.form;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
	@Size(max = 20)
	private String loginId;
	@Size(max = 20)
	private String password;
}