package com.justlife.casestudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justlife.casestudy.dto.request.UserLoginRequestDTO;
import com.justlife.casestudy.dto.request.UserRegisterRequestDTO;
import com.justlife.casestudy.dto.response.APIResponseDTO;
import com.justlife.casestudy.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 * @author Mukesh.K
 *
 */
@Tag(name = "Authentication", description = "User registration and login APIs")
@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private IUserService userService;

	@Operation(summary = "Register user", description = "Creates a new user account")
	@PostMapping("/register")
	public ResponseEntity<APIResponseDTO> register(@RequestBody UserRegisterRequestDTO dto) {
		logger.info("API Register hit");
		APIResponseDTO response = new APIResponseDTO();
		response.setData(userService.register(dto));
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Login user", description = "Returns a JWT token")
	@PostMapping("/login")
	public ResponseEntity<APIResponseDTO> login(@RequestBody UserLoginRequestDTO dto) {
		logger.info("API Login hit");
		APIResponseDTO response = new APIResponseDTO();
		response.setData(userService.login(dto));
		return ResponseEntity.ok(response);
	}
}
