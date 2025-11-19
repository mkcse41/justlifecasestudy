package com.justlife.casestudy.service;

import com.justlife.casestudy.dto.request.UserLoginRequestDTO;
import com.justlife.casestudy.dto.request.UserRegisterRequestDTO;
import com.justlife.casestudy.dto.response.UserResponseDTO;

/**
 * 
 * @author Mukesh.K
 *
 */
public interface IUserService {

	UserResponseDTO register(UserRegisterRequestDTO dto);

	String login(UserLoginRequestDTO dto);

}
