package com.justlife.casestudy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.justlife.casestudy.constants.ErrorMsgConstants;
import com.justlife.casestudy.dto.request.UserLoginRequestDTO;
import com.justlife.casestudy.dto.request.UserRegisterRequestDTO;
import com.justlife.casestudy.dto.response.UserResponseDTO;
import com.justlife.casestudy.exceptions.DataAlreadyExistsException;
import com.justlife.casestudy.mapper.UserMapper;
import com.justlife.casestudy.model.User;
import com.justlife.casestudy.repository.UserRepository;
import com.justlife.casestudy.security.JwtUtils;
import com.justlife.casestudy.service.IUserService;

/**
 * 
 * @author Mukesh.K
 *
 */
@Service
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserResponseDTO register(UserRegisterRequestDTO dto) {

		logger.info("User registration started: {}", dto.getUsername());

		if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
			logger.error("Username already exists: {}", dto.getUsername());
			throw new DataAlreadyExistsException(ErrorMsgConstants.USERNAME_ALREADY_EXISTS);
		}

		User user = new User();
		user.setUsername(dto.getUsername());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setFullName(dto.getFullName());
		user.setRole(dto.getUserType());
		userRepository.save(user);

		logger.info("User registered successfully: {}", dto.getUsername());
		return userMapper.convertUserEntityToResponse(user);
	}

	@Override
	public String login(UserLoginRequestDTO dto) {

		logger.info("Login attempt for user: {}", dto.getUsername());

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

		return jwtUtils.generateToken(dto.getUsername());
	}
}
