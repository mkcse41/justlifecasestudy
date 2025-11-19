package com.justlife.casestudy.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Mukesh
 *
 */
@Getter
@Setter
public class UserRegisterRequestDTO {

	private String username;
	private String password;
	private String fullName;
	private String userType;

}
