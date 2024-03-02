package com.reporting.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.reporting.response.GenericResponse;
import com.reporting.response.GenericResponseBody;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(Exception.class)
	public ResponseEntity<GenericResponse> customException(Exception exception){
		
		log.error("Some error occured | MESSAGE => "+exception.getMessage());
		
		String message = "FAILURE";
		
		GenericResponse response = new GenericResponse();
		response.setData(null);
		response.setResponse(GenericResponseBody.builder()
				.date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.status(message)
				.description(exception.getMessage())
				.build()
				);
		
		return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		
	}

}
