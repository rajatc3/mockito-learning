package com.rajat.mockitolearning.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "DEVICE_MASTER")
@Data
@Builder
public class AuthenticationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deviceId;

	@NonNull
	private String userIdentifier;

	@NonNull
	private String osType;

	@NonNull
	private String osVersion;

	@NonNull
	private String tokenId;
	
	private Boolean isExpired;
}
