package com.mbathegamer.budgetto.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mbathegamer.budgetto.dtos.LoginRequest;
import com.mbathegamer.budgetto.dtos.RegisterRequest;
import com.mbathegamer.budgetto.mappers.UserMapper;
import com.mbathegamer.budgetto.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserService service;
  private final UserMapper mapper;

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

  @PostMapping("login")
  public ResponseEntity<?> login(
      @Valid
      @RequestBody
      LoginRequest request) {
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    return ResponseEntity.ok().build();
  }
}
