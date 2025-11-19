package com.justlife.casestudy.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author Mukesh.Kumar
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleResourceNotFoundException(DataNotFoundException ex, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
		response.setMessage(ex.getMessage());
		response.setPath(request.getRequestURI());
		return response;
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
		response.setMessage(ex.getMessage());
		response.setPath(request.getRequestURI());
		return response;
	}

	@ExceptionHandler(DataAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ErrorResponse handleDataAlreadyExistsException(DataAlreadyExistsException ex, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setError(HttpStatus.CONFLICT.getReasonPhrase());
		response.setMessage(ex.getMessage());
		response.setPath(request.getRequestURI());
		return response;
	}

	@ExceptionHandler(InternalServerErrorException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse handleInternalServerExceptionException(InternalServerErrorException ex,
			HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		response.setMessage(ex.getMessage());
		response.setPath(request.getRequestURI());
		return response;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		response.setMessage(ex.getMessage());
		response.setPath(request.getRequestURI());
		return response;
	}

	@ExceptionHandler(BadUserCredentialException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse handleBadUserCredentailException(BadUserCredentialException ex, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
		response.setMessage(ex.getMessage());
		response.setPath(request.getRequestURI());
		return response;
	}

}
