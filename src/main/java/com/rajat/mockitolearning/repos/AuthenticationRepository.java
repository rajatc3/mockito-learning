package com.rajat.mockitolearning.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajat.mockitolearning.entity.AuthenticationEntity;

public interface AuthenticationRepository extends JpaRepository<AuthenticationEntity, Long> {

	public Optional<AuthenticationEntity> findByUserIdentifierEqualsAndIsExpiredFalse(String userIdentifier);
}
