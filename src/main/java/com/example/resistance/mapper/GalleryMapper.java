package com.example.resistance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.resistance.form.GalleryForm;

@Mapper
public interface GalleryMapper {

	// ギャラリー情報登録
	public void registGallery(GalleryForm galleryForm);

	// ギャラリー情報取得
	public List<GalleryForm> getGallery();

}
