package com.security.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.entity.UserCredentials;

public interface UserCredentialRepo extends JpaRepository<UserCredentials, Integer>{

	Optional<UserCredentials> findByName(String username);

}
