package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.security.dto.AuthRequest;
import com.security.entity.UserCredentials;
import com.security.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public String addNewUser(@RequestBody UserCredentials credentials) {
		return service.saveUser(credentials);
	}
	
	@PostMapping("/token")
	public String getToken(@RequestBody AuthRequest authRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		
		if(authenticate.isAuthenticated()) {
			UserCredentials principal = (UserCredentials)authenticate.getPrincipal();
			return service.generateToken(authRequest.getUserName(), String.valueOf(principal.getId()));
		}else {
			throw new BadCredentialsException("Invalid credentials");
		}	
	}
	
	@GetMapping("/validate")
	public String validateToken(@RequestParam String token) {
		service.validateToken(token);
		return "Token is valid";
	}

}
