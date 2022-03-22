package com.rajat.mockitolearning.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajat.mockitolearning.entity.AuthenticationEntity;
import com.rajat.mockitolearning.repos.AuthenticationRepository;
import com.rajat.mockitolearning.utility.RequestObject;

@Service
public class AuthenticationService {

	@Autowired
	AuthenticationRepository authRepo;

	public boolean authenticateRequest(RequestObject request) {
		Optional<AuthenticationEntity> authDetails = authRepo
				.findByUserIdentifierEqualsAndIsExpiredFalse(request.getUserIdentifier());

		if (authDetails.isPresent()) {
			if (authDetails.get().getTokenId().equals(request.getTokenId())) {
				return true;
			}
		}
		return false;
	}

}
