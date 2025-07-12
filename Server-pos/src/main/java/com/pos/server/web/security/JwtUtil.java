package com.pos.server.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String KEY = "posserver11c16375812192317TYHEWPUGnxoldsaDJSA412";

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(SignatureAlgorithm.HS256, KEY).compact();
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
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
}
