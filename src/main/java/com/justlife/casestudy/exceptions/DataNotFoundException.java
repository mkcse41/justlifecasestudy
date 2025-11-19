package com.justlife.casestudy.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Mukesh.Kumar
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ErrorResponse errorResponse;

	public DataNotFoundException(String message) {
		super(message);
		errorResponse = ErrorResponse.builder().message(message).build();

	}
}
