package com.capstone.project.payload;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import com.capstone.project.bean.enumeration.Scopes;
import com.capstone.project.exceptions.JwtExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * RefreshToken
 * 
 * @author vladimir.stankovic
 *
 * Aug 19, 2016
 */
@SuppressWarnings("unchecked")
public class RefreshToken {
    private Jws<Claims> claims;
    
    @Autowired
    private AuthenticationResponse authenticationResponse;

    private RefreshToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    /**
     * Creates and validates Refresh token 
     * 
     * @param token
     * @param signingKey
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     * @return
     */
    public static RefreshToken create(AuthenticationResponse token, String signingKey) {
        
    	Jws<Claims> claims = token.parseClaims(signingKey);
    	
        List<String> scopes = claims.getBody().get("scopes", List.class);
        if (scopes == null || scopes.isEmpty() 
                || !scopes.stream().filter(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope)).findFirst().isPresent()) {
            //return null;
        }	

        return new RefreshToken(claims);
    }

    
    public String getToken() {
        return authenticationResponse.getJwt();
    }

    public Jws<Claims> getClaims() {
        return claims;
    }
    
    public String getJti() {
        return claims.getBody().getId();
    }
    
    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
