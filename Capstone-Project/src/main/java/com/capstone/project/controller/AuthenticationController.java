package com.capstone.project.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.capstone.project.bean.Role;
import com.capstone.project.bean.User;
import com.capstone.project.bean.enumeration.RoleName;
import com.capstone.project.config.Constants;
import com.capstone.project.exceptions.AppException;
import com.capstone.project.repository.RoleRepository;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.payload.*;
import com.capstone.project.service.AuthenticationService;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
//@CrossOrigin("*")
@RequestMapping("/private")
public class AuthenticationController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
	
	@PostMapping("/signin")
	public ResponseEntity<?> getAccessToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		
		String userName = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		
//		try {
//			authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//			);
//		}
//		catch (BadCredentialsException e) {
//			throw new Exception("Incorrect username or password", e);
//		}
//
//
//		final UserDetails userDetails = userDetailsService
//				.loadUserByUsername(authenticationRequest.getUsername());
//
//		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		AuthenticationResponse accessToken = authenticationService.createAccessJWTToken(userName, password);
		AuthenticationResponse refreshToken = authenticationService.createRefreshJWTToken(userName, password);
		
		Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("token", accessToken.getJwt());
        tokenMap.put("refreshToken", refreshToken.getJwt());

		//return ResponseEntity.ok(authenticationService.generateJWTToken(userName, password));
        return ResponseEntity.ok(tokenMap);
		
	}
	
	@PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
	
	@PostMapping("/auth/token")
	public @ResponseBody AuthenticationResponse getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ExpiredJwtException {
		String tokenPayload = request.getHeader("Authorization").substring("Bearer ".length(), request.getHeader("Authorization").length());
        //RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        AuthenticationResponse rawToken = new AuthenticationResponse(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, Constants.SECRET_KEY);
        //String jti = refreshToken.getJti();
        System.out.println("Refresh token :: "+refreshToken.toString());
        String subject = refreshToken.getSubject();
        User user = userRepository.findByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");

        return authenticationService.createAccessJWTToken(user.getName(), user.getPassword());
    }


}
