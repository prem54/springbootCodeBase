package com.capstone.project.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.capstone.project.security.MyUserDetailsService;
import com.capstone.project.security.models.AuthenticationRequest;
import com.capstone.project.security.models.AuthenticationResponse;
import com.capstone.project.security.util.JwtUtil;

@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	public AuthenticationResponse generateJWTToken(String userName, String password) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return new AuthenticationResponse(jwt);
		
	}

}
