package com.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import com.gateway.util.JwtUtil;

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
	
	@Autowired
	private RouteValidator routeValidator;
	
//	@Autowired
//	private RestTemplate template;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	public AuthenticationFilter() {
		super(Config.class);
	}

	public static class Config{
		
	}

	@Override
	public GatewayFilter apply(Config config) {
		
		return ((exchange, chain) -> {
			
			if(routeValidator.isSecured.test(exchange.getRequest())) {
				if(exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("Missing authorization header");
				}
				
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				
				if(authHeader!=null && authHeader.startsWith("Bearer ")) {
					authHeader=authHeader.substring(7);
				}
				
				try {
//					template.getForObject("http://AUTH-SERVICE//validate?"+authHeader, String.class);
					
					jwtUtil.validateToken(authHeader);
						
				}catch (Exception e) {
					throw new RuntimeException("Unauthorized access");
				}
				
			}
			
			return chain.filter(exchange);
		});
	}
}
