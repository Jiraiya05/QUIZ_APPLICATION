package com.security.utility;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class Utility {
	
	public static String generateJwtSecret(int length) {
		
		SecureRandom secureRandom = new SecureRandom();
		byte secretBytes[] = new byte[length];
		secureRandom.nextBytes(secretBytes);
		
		String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(secretBytes);
		
		return secret;
		
	}

}
