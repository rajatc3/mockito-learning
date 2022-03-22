package com.rajat.mockitolearning.utility;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestObject {
	@NonNull
	private String userIdentifier;
	
	@NonNull
	private String osType;
	
	@NonNull
	private String osVersion;
	
	@NonNull
	private String tokenId;
	
	private Object data;
}
