package com.mbathegamer.budgetto.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.config.JwtConfig;
import com.mbathegamer.budgetto.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {
  private final JwtConfig jwtConfig;

  public String generateAccessToken(User user) {
    return generateToken(user, jwtConfig.getAccessTokenExpiration());
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, jwtConfig.getRefreshTokenExpiration());
  }

  private String generateToken(User user, Long tokenExpiration) {
    return Jwts.builder()
        .subject(user.getId().toString())
        .claim("email", user.getEmail())
        .claim("first-name", user.getFirstName())
        .claim("last-name", user.getLastName())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1_000 * tokenExpiration))
        .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
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
        .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Long getUserIdFromToken(String token) {
    return Long.parseLong(getClaims(token).getSubject());
  }
}
