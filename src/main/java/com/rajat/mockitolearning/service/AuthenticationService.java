package com.rajat.mockitolearning.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajat.mockitolearning.entity.AuthenticationEntity;
import com.rajat.mockitolearning.repos.AuthenticationRepository;

@Service
public class AuthenticationService {

	@Autowired
	AuthenticationRepository authRepo;

	public boolean authenticateRequest(String userIdentifier, String tokenId) {
		Optional<AuthenticationEntity> authDetails = authRepo
				.findByUserIdentifierEqualsAndIsExpiredFalse(userIdentifier);

		if (authDetails.isPresent()) {
			if (authDetails.get().getTokenId().equals(tokenId)) {
				return true;
			}
		}
		return false;
	}

}
