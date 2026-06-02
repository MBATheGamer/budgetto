package com.mbathegamer.budgetto.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mbathegamer.budgetto.config.JwtConfig;
import com.mbathegamer.budgetto.dtos.JwtResponse;
import com.mbathegamer.budgetto.dtos.LoginRequest;
import com.mbathegamer.budgetto.dtos.RegisterRequest;
import com.mbathegamer.budgetto.dtos.UserResponse;
import com.mbathegamer.budgetto.entities.User;
import com.mbathegamer.budgetto.mappers.UserMapper;
import com.mbathegamer.budgetto.services.JwtService;
import com.mbathegamer.budgetto.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserService service;
  private final UserMapper mapper;
  private final JwtService jwtService;
  private final JwtConfig jwtConfig;

  @PostMapping("/register")
  public ResponseEntity<?> regiter(
      @Valid
      @RequestBody
      RegisterRequest request,
      UriComponentsBuilder uriBuilder) {
    var user = service.register(request).orElse(null);

    if (user == null) {
      return ResponseEntity
          .badRequest()
          .body(Map.of("email", "Email is already registered."));
    }

    var userResponse = mapper.toDto(user);
    var uri = uriBuilder
        .path("/users/{id}")
        .buildAndExpand(userResponse.id())
        .toUri();

    return ResponseEntity
        .created(uri)
        .body(userResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(
      @Valid
      @RequestBody
      LoginRequest request,
      HttpServletResponse response) {
    authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

    var user = (User) service.loadUserByUsername(request.email());
    var accessToken = jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    var cookie = new Cookie("refresh-token", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/api/v1/auth/refresh");
    cookie.setMaxAge((int) jwtConfig.getRefreshTokenExpiration());
    cookie.setSecure(true);
    response.addCookie(cookie);

    return ResponseEntity.ok(new JwtResponse(accessToken));
  }

  @PostMapping("/refresh")
  public ResponseEntity<JwtResponse> refresh(
      @CookieValue(value = "refresh-token")
      String refreshToken) {
    if (!jwtService.validateToken(refreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    var userId = jwtService.getUserIdFromToken(refreshToken);
    var user = service.findById(userId).orElseThrow();
    var accessToken = jwtService.generateAccessToken(user);

    return ResponseEntity.ok(new JwtResponse(accessToken));
  }

  @PostMapping("/validate")
  public boolean validate(
      @RequestHeader("Authorization")
      String authHeader) {
    var token = authHeader.replace("Bearer ", "");
    System.out.println("Hi");
    return jwtService.validateToken(token);
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> me() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userId = (Long) authentication.getPrincipal();

    var user = service.findById(userId).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(user));
  }

  @PostMapping("/logout")
  public void logout(HttpServletResponse response) {
    var cookie = new Cookie("refresh-token", "");
    cookie.setHttpOnly(true);
    cookie.setPath("/api/v1/auth/refresh");
    cookie.setMaxAge(0);
    cookie.setSecure(false);

    response.addCookie(cookie);
  }
}
