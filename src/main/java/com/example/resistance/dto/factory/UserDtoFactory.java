package com.example.resistance.dto.factory;

import org.springframework.stereotype.Component;

import com.example.resistance.dto.UserDto;
import com.example.resistance.entity.User;

@Component
public class UserDtoFactory {

	/** create dto from entity */
	public UserDto create(User entity) {
		if (entity == null) {
			return null;
		}
		return new UserDto(
				entity.getId(),
				entity.getLoginId(),
				entity.getPassword(),
				entity.getName(),
				entity.getCreatedDate()
		);
	}

}