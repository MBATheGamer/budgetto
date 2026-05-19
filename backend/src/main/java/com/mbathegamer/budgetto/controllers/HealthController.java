package com.mbathegamer.budgetto.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.mbathegamer.budgetto.dtos.HealthResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController()
@RequestMapping("/health")
public class HealthController {
  @GetMapping
  public ResponseEntity<HealthResponse> health() {
    return ResponseEntity.ok(new HealthResponse("alive"));
  }
}
