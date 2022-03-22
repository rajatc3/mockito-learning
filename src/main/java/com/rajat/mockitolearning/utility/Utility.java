package com.rajat.mockitolearning.utility;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utility {
	public static final String SUCCESS_MESSAGE = "Request Processed successfully.";
	public static final String BAD_REQUEST_MESSAGE = "Bad Request";

	public static <T> ResponseObject createSuccessResponseObject(T responseData) {

		return ResponseObject.builder().status(HttpStatus.OK).timestamp(LocalDateTime.now()).data(responseData)
				.message(SUCCESS_MESSAGE).build();
	}

	public static ResponseObject createBadRequestResponseObject() {
		return ResponseObject.builder().status(HttpStatus.BAD_REQUEST).timestamp(LocalDateTime.now()).data(null)
				.message(BAD_REQUEST_MESSAGE).build();
	}
	
	public static ResponseEntity<ResponseObject> success(ResponseObject body) {
		return ResponseEntity.ok(body);
	}

	public static ResponseEntity<ResponseObject> badRequest(ResponseObject body) {
		return ResponseEntity.badRequest().body(body);
	}
}