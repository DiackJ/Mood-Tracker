package com.springboot.MoodTracker.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
//class responsible for setting up the JWT token generation and validation
public class JwtUtil {
    //access jwt key
    @Value("${JWT_SECRET}")
    private String jwtKey;

    //create the JWT token
    public String createToken(Map<String, Object> claims, String userEmail){
        return Jwts.builder()
                .claims(claims)
                .subject(userEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    //generate the actual token
    public String generateToken(String userEmail){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userEmail);
    }

    //extract all the claims from the token metadata
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    //extract individual claims from token metadata
    public <T> T extractClaim(String token, Function<Claims,T> resolveClaims){
        final Claims claims = extractAllClaims(token);
        return resolveClaims.apply(claims);
    }

    //extract token expiration from token payload
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //extract the user's email from the token payload
    public String extractSubject(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //validate expiration
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
        //returns true if the expiration date was before the current date
        //else returns false as it is not expired
    }

    //validate that the token is non expired and belongs to the current user's session
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = userDetails.getUsername(); //extracts user's email from user details wrapper
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
