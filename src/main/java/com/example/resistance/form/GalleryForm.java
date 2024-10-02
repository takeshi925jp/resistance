package com.example.resistance.form;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GalleryForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String gallery_path;
	private String creator;
	private String gallery_name;
	private String picture_data;

	public GalleryForm() {}

	public GalleryForm(Integer id, String gallery_path, String creator, String gallery_name, String picture_data) {
		super();

		this.id = id;
		this.gallery_path= gallery_path;
		this.creator = creator;
		this.gallery_name = gallery_name;
		this.picture_data = picture_data;
	}
}
