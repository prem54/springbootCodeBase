package com.capstone.project.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.capstone.project.payload.AuthenticationResponse;

public class JwtExpiredTokenException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;
    
    private AuthenticationResponse token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(AuthenticationResponse token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getJwt();
    }
}
