package com.example.resistance.form;

import java.io.Serializable;

import lombok.Data;

@Data
public class RequestForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String text;
}
