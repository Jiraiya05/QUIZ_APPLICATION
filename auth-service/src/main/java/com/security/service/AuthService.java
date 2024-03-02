package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.entity.UserCredentials;
import com.security.repo.UserCredentialRepo;

@Service
public class AuthService {
	
	@Autowired
	private UserCredentialRepo repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	public String saveUser(UserCredentials userCredentials) {
		userCredentials.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
		repository.save(userCredentials);
		return "User added";
	}
	
	public String generateToken(String userName, String userId) {
		return jwtService.generateToken(userName, userId);
	}
	
	public void validateToken(String token) {
		jwtService.validateToken(token);
	}

}
