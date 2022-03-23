package com.rajat.mockitolearning.utility;

import org.springframework.lang.NonNull;

import com.rajat.mockitolearning.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestObject {
	@NonNull
	private String userIdentifier;
	
	@NonNull
	private String osType;
	
	@NonNull
	private String osVersion;
	
	@NonNull
	private String tokenId;
	
	private Book data;
}
