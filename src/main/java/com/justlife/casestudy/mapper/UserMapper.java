package com.justlife.casestudy.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justlife.casestudy.dto.response.UserResponseDTO;
import com.justlife.casestudy.model.User;

/**
 * 
 * @author Mukesh
 *
 */
@Component
public class UserMapper {

	@Autowired
	private ModelMapper modelMapper;

	public UserResponseDTO convertUserEntityToResponse(User user) {
		return modelMapper.map(user, UserResponseDTO.class);
	}

}
