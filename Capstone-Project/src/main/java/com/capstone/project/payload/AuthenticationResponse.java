package com.capstone.project.payload;

import java.io.Serializable;

import org.springframework.security.authentication.BadCredentialsException;

import com.capstone.project.exceptions.JwtExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }
    
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.jwt);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        }
    }

    public String getJwt() {
        return jwt;
    }

}
