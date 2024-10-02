package com.example.resistance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.resistance.entity.Member;
import com.example.resistance.entity.Room;



@Mapper
public interface RoomMapper {

	// room情報取得
	public List<Room> showRooms();

	// room情報取得
	public List<Room> showPaintRooms();

	// room情報取得
	public Room showRoom(Room room);

	// room作成
	public void createRoom(Room room);

	// Member作成
	public int createMember(Member member);

	// Member情報取得
	public List<Member> showMember(Room room);

	// 参加人数更新
	public void updateMemberNum(Room room);

	// 部屋削除
	public void deleteRoom(Room room);

	// 退室
	public void exitRoom(Member roomMember);
}