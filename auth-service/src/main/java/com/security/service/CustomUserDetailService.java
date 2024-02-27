package com.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.security.config.CustomUserDetails;
import com.security.entity.UserCredentials;
import com.security.repo.UserCredentialRepo;

public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserCredentialRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserCredentials> credential = repo.findByName(username);
		
		return credential.map(CustomUserDetails::new).orElseThrow(()->new UsernameNotFoundException("No user found for given credentials"));
	}

}
