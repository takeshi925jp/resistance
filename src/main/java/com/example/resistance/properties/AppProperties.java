package com.example.resistance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@PropertySource("./config/resistance.properties")
@ConfigurationProperties(prefix="app")
public class AppProperties {

	// ルーム種別（チャットルーム）
	private int kindChatRoom;
	// ルーム種別（ペイントルーム）
	private int kindPaintRoom;
	// パスワード照会失敗時のメッセージ
	private String passMissingMessage;
	// ルーム人数超過時のメッセージ
	private String messageOverRoomSize;
	// パスワードの正規表現
	private String passwordPattern;
	// パスワード入力パターン失敗
	private String missingPasswordPattern;
	// 参加可能人数フォーマット不正
	private String numberFormatError;
	// 描画の保存先のパス
	private String galleryPath;
	// 描画の保存先のパス（ローカル）
	private String galleryPathLocal;
	// 描画の拡張子
	private String paintExtension;
}
