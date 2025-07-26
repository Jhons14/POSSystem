package com.pos.server.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    
    @Value("${security.jwt.secret:posserver11c16375812192317TYHEWPUGnxoldsaDJSA412}")
    private String jwtSecret;
    
    @Value("${security.jwt.expiration-time:86400000}")
    private long jwtExpirationTime;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }


    public boolean validateToken (String token, UserDetails userDetails){
    return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpirated(token);
    }

    public String extractUsername (String token){
        return getClaims(token).getSubject();
    }

    public boolean isTokenExpirated (String token){
        return getClaims(token).getExpiration().before(new Date());
    }

    public Claims getClaims (String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}
