package com.mbathegamer.budgetto.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${spring.jwt.secret}")
  private String secret;

  @Value("${spring.jwt.token-expiration}")
  private long tokenExpiration;

  public String generateToken(String email) {
    return Jwts.builder()
        .subject(email)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1_000 * tokenExpiration))
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      return getClaims(token).getExpiration().after(new Date());
    } catch (JwtException exception) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getEmailFromToken(String token) {
    return getClaims(token).getSubject();
  }
}
