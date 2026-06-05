package com.mbathegamer.budgetto.controllers;

import java.util.List;

import org.apache.coyote.BadRequestException;
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

import com.mbathegamer.budgetto.dtos.TransactionRequest;
import com.mbathegamer.budgetto.dtos.TransactionResponse;
import com.mbathegamer.budgetto.mappers.TransactionMapper;
import com.mbathegamer.budgetto.services.JwtService;
import com.mbathegamer.budgetto.services.TransactionService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
  private final TransactionService transactionService;
  private final JwtService jwtService;
  private final TransactionMapper mapper;

  @PostMapping
  public ResponseEntity<TransactionResponse> create(
      @Valid
      @RequestBody
      TransactionRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader,
      UriComponentsBuilder uriBuilder) throws BadRequestException {
    var userId = getUserId(authorizationHeader);
    var response = transactionService.create(userId, request)
        .orElseThrow(() -> new BadRequestException("Can't create transaction"));

    var uri = uriBuilder
        .path("/transactions/{id}")
        .buildAndExpand(response.getId())
        .toUri();

    return ResponseEntity.created(uri).body(mapper.toDto(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionResponse> getById(
      @PathVariable
      Long id,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var userId = getUserId(authorizationHeader);
    var transaction = transactionService.findById(id, userId).orElse(null);

    if (transaction == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(transaction));
  }

  @GetMapping
  public ResponseEntity<List<TransactionResponse>> getByUser(
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var userId = getUserId(authorizationHeader);
    var response = transactionService.findByUser(userId);

    var transaction = response.stream().map(mapper::toDto).toList();

    return ResponseEntity.ok(transaction);
  }
  @PutMapping("/{id}")
  public ResponseEntity<TransactionResponse> update(
      @PathVariable
      Long id,
      @RequestBody
      TransactionRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader) throws BadRequestException {
    var userId = getUserId(authorizationHeader);
    var transaction = transactionService.update(id, userId, request)
        .orElseThrow(() -> new BadRequestException("Can't update transaction"));

    return ResponseEntity.ok(mapper.toDto(transaction));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable
      Long id,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var userId = getUserId(authorizationHeader);
    transactionService.delete(id, userId);

    return ResponseEntity.noContent().build();
  }

  private Long getUserId(String authorizationHeader) {
    var token = authorizationHeader.replace("Bearer ", "");
    return jwtService.getUserIdFromToken(token);
  }
}
