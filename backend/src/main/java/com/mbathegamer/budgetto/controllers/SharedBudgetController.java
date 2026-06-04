package com.mbathegamer.budgetto.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mbathegamer.budgetto.dtos.SharedBudgetRequest;
import com.mbathegamer.budgetto.dtos.SharedBudgetResponse;
import com.mbathegamer.budgetto.mappers.SharedBudgetMapper;
import com.mbathegamer.budgetto.services.JwtService;
import com.mbathegamer.budgetto.services.SharedBudgetService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/shared-budgets")
public class SharedBudgetController {
  private final SharedBudgetService service;
  private final SharedBudgetMapper mapper;
  private final JwtService jwtService;

  @PostMapping()
  public ResponseEntity<SharedBudgetResponse> create(
      @Valid
      @RequestBody
      SharedBudgetRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader,
      UriComponentsBuilder uriBuilder) throws Exception {
    var response = service.create(getUserId(authorizationHeader), request)
        .orElseThrow(() -> new Exception("Can't create category"));

    var budgetResponse = mapper.toDto(response);

    var uri = uriBuilder
        .path("/budgets/{id}")
        .buildAndExpand(budgetResponse.id())
        .toUri();

    return ResponseEntity
        .created(uri)
        .body(budgetResponse);
  }

  @GetMapping()
  public ResponseEntity<List<SharedBudgetResponse>> getAllByUser(
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var response = service.findByUserId(getUserId(authorizationHeader));

    var budgets = response.stream().map(mapper::toDto).toList();

    return ResponseEntity.ok(budgets);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SharedBudgetResponse> getById(
      @PathVariable
      Long id,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var budget = service.findById(id, getUserId(authorizationHeader)).orElse(null);

    if (budget == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(budget));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SharedBudgetResponse> update(
      @PathVariable
      Long id,
      @RequestBody
      SharedBudgetRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var budget = service.update(id, getUserId(authorizationHeader), request).orElse(null);

    if (budget == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(budget));
  }

  @DeleteMapping("/{id}")
  public void delete(
      @PathVariable
      Long id,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    service.delete(id, getUserId(authorizationHeader));
  }

  private Long getUserId(String authorizationHeader) {
    var token = authorizationHeader.replace("Bearer ", "");
    return jwtService.getUserIdFromToken(token);
  }
}
