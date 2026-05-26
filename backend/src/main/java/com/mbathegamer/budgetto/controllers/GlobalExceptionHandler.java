package com.mbathegamer.budgetto.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationError(
      MethodArgumentNotValidException exception) {
    var errors = new HashMap<String, String>();

    exception.getBindingResult().getFieldErrors().forEach(error -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<String> handleDisabled() {
    return ResponseEntity.status(403).body("Account is pending approval.");
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<String> handleLocked() {
    return ResponseEntity.status(403).body("Account is blocked.");
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentials() {
    return ResponseEntity.status(401).body("Invalid email or password.");
  }
}
