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
public class DataAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ErrorResponse errorResponse;

	public DataAlreadyExistsException(String message) {

		super(message);

		errorResponse = ErrorResponse.builder().message(message).build();

	}
}
