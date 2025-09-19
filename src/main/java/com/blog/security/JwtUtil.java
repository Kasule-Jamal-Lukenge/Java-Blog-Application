package com.blog.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
//    private final Key key;
//    private final long expiryMs;
//    public JwtUtil(
//            @Value("${app.jwt.secret}")String secret,
//            @Value ("${app.jwt.expiration-ms}")long expiryMs) {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//        this.expiryMs = expiryMs;
//    }

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expiryMs = 1000 * 60 * 60;

    public String generateToken(String username) {
        var now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+expiryMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
