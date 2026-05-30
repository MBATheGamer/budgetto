package com.mbathegamer.budgetto.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.entities.User;

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

  public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getId().toString())
        .claim("email", user.getEmail())
        .claim("first-name", user.getFirstName())
        .claim("last-name", user.getLastName())
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

  public Long getUserIdFromToken(String token) {
    return Long.parseLong(getClaims(token).getSubject());
  }
}
