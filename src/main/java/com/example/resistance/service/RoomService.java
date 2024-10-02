package com.example.resistance.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resistance.entity.Member;
import com.example.resistance.entity.Room;
import com.example.resistance.mapper.RoomMapper;

@Mapper
@Service
public class RoomService {

		@Autowired
		private RoomMapper roomMapper;

		public void createRoom(Room room ) {

			// room作成
			roomMapper.createRoom(room);
		}

		public List<Room> showRooms() {

			// room取得
			List<Room> rooms = roomMapper.showRooms();

			return rooms;
		}

		public List<Room> showPaintRooms() {

			// room取得
			List<Room> rooms = roomMapper.showPaintRooms();

			return rooms;
		}

		public Room showRoom(Room room) {

			// room取得
			Room resultRoom = roomMapper.showRoom(room);

			return resultRoom;
		}

		public int createMember(Member member) {

			// member作成、自動採番されたIDを返す
			int result = roomMapper.createMember(member);

			return result;
		}

		public List<Member> showMember(Room room) {

			// roomに対応するplayer取得
			List<Member> members = roomMapper.showMember(room);

			return members;
		}

		public void updateMemberNum(Room room) {

			// 参加人数更新
			roomMapper.updateMemberNum(room);
		}

		public void deleteRoom(Room room) {

			// 参加人数更新
			roomMapper.deleteRoom(room);
		}

		public void exitRoom(Member member) {

			// 参加人数更新
			roomMapper.exitRoom(member);
		}

}
