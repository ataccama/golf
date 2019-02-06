package com.ataccama.golf.gateway.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {
	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String badRequest(BadRequestException e) {
		log.warn("Bad Request", e);
		return e.getMessage();
	}

	@ExceptionHandler(NotFountException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String notFound(NotFountException e) {
		log.warn("Not Found", e);
		return e.getMessage();
	}
}
