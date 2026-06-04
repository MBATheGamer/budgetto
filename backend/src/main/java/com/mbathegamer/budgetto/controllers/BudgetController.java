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

import com.mbathegamer.budgetto.dtos.BudgetRequest;
import com.mbathegamer.budgetto.dtos.BudgetResponse;
import com.mbathegamer.budgetto.mappers.BudgetMapper;
import com.mbathegamer.budgetto.services.BudgetService;
import com.mbathegamer.budgetto.services.JwtService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/budgets")
public class BudgetController {
  private final BudgetService service;
  private final BudgetMapper mapper;
  private final JwtService jwtService;

  @PostMapping()
  public ResponseEntity<BudgetResponse> create(
      @Valid
      @RequestBody
      BudgetRequest request,
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
  public ResponseEntity<List<BudgetResponse>> getAllByUser(
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var response = service.findByUserId(getUserId(authorizationHeader));

    var budgets = response.stream().map(mapper::toDto).toList();

    return ResponseEntity.ok(budgets);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BudgetResponse> getById(
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
  public ResponseEntity<BudgetResponse> update(
      @PathVariable
      Long id,
      @RequestBody
      BudgetRequest request,
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
