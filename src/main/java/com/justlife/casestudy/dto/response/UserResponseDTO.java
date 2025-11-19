package com.justlife.casestudy.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Mukesh.K
 *
 */
@Setter
@Getter
public class UserResponseDTO {

	private Long id;
	private String username;
	private String fullName;
	private String role;

}
