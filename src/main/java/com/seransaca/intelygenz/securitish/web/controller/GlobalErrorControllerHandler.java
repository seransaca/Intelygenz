package com.seransaca.intelygenz.securitish.web.controller;

import com.seransaca.intelygenz.securitish.service.exceptions.*;
import com.seransaca.intelygenz.securitish.web.dto.error.ApiError;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Log4j2
public class GlobalErrorControllerHandler {

	@ExceptionHandler({SafeboxNotFoundException.class})
	public ResponseEntity<ApiError> safeboxNotFoundException(SafeboxNotFoundException ex) {
		log.warn(ex);
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Requested safebox does not exist", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> validationErrorException(MethodArgumentNotValidException ex) {
		log.warn("Validation error: {}", ex.getBindingResult().getAllErrors());
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error", ex);
		apiError.addValidationErrors(ex.getBindingResult());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		String msg = String.format("The field '%s' with the value '%s' is wrong", ex.getName(), ex.getValue());
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, msg, ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ApiError> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Don't appears the param " + ex.getParameterName(), ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> httpMessageNotReadable(HttpMessageNotReadableException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "JSON mal formado", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MalformedDataException.class)
	public ResponseEntity<ApiError> malformedDataError(MalformedDataException ex) {
		ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Malformed expected data", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> apiGenericError(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected API error", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ApiError> apiGenericError(LockedException ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected API error", ex);
		return buildResponseEntity(apiError);
	}


	@ExceptionHandler(CypherException.class)
	public ResponseEntity<ApiError> apiGenericError(CypherException ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected API error", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiError> unauthorizedError(UnauthorizedException ex) {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Specified token does not match", ex);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> accessDeniedError(AccessDeniedException ex) {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Specified token does not match", ex);
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getHttpStatus());
	}

}
