package com.capstone.project.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.capstone.project.security.MyUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.capstone.project.bean.enumeration.Scopes;
import com.capstone.project.config.Constants;
import com.capstone.project.payload.*;
import com.capstone.project.security.JwtUtil;

@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	public AuthenticationResponse createAccessJWTToken(String userName, String password) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		System.out.println("JWT Token : "+jwt);
		
		
		return new AuthenticationResponse(jwt);
		
	}
	
	public AuthenticationResponse createRefreshJWTToken(String userName, String password) {
        if (StringUtils.isBlank(userName)) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
        
        String token = Jwts.builder()
          .setClaims(claims)
          .setId(UUID.randomUUID().toString())
          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
          .setExpiration(Date.from(currentTime
              .plusMinutes(Constants.REFRESH_TOKEN_EXP_TIME)
              .atZone(ZoneId.systemDefault()).toInstant()))
          .signWith(SignatureAlgorithm.HS512, Constants.SECRET_KEY)
        .compact();

        return new AuthenticationResponse(token);
    }

}
