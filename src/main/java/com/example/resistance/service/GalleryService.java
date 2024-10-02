package com.example.resistance.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resistance.form.GalleryForm;
import com.example.resistance.mapper.GalleryMapper;

@Mapper
@Service
public class GalleryService {
	@Autowired
	private GalleryMapper galleryMapper;

	/*
	 * ギャラリー情報登録
	 */
	public void registGallery(GalleryForm galleryForm) {

		// ギャラリー情報登録
		galleryMapper.registGallery(galleryForm);
	}

	/*
	 * ギャラリー情報取得
	 */
	public List<GalleryForm> getGallery(){

		// ギャラリー情報取得
		List<GalleryForm> gallerys = galleryMapper.getGallery();

		return gallerys;
	};
}
