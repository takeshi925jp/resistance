package com.example.resistance.form;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Canvas implements Serializable {

	private static final long serialVersionUID = 1L;

	private String canvasData;
	private String roomId;

	public Canvas() {
	}

	public Canvas(String canvasData, String roomId) {
		super();

		this.canvasData = canvasData;
		this.roomId = roomId;
	}

}
