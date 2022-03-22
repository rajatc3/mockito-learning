package com.rajat.mockitolearning.utility;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseObject {
	private HttpStatus status;
	private LocalDateTime timestamp;
	private Object data;
	private String message;
}
