package com.mbathegamer.budgetto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtConfig {
  private String secret;
  private long accessTokenExpiration;
  private long refreshTokenExpiration;
}
