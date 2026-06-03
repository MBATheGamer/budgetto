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

import com.mbathegamer.budgetto.dtos.CategoryRequest;
import com.mbathegamer.budgetto.dtos.CategoryResponse;
import com.mbathegamer.budgetto.mappers.CategoryMapper;
import com.mbathegamer.budgetto.services.CategoryService;
import com.mbathegamer.budgetto.services.JwtService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {
  private final CategoryService service;
  private final CategoryMapper mapper;
  private final JwtService jwtService;

  @PostMapping()
  public ResponseEntity<CategoryResponse> create(
      @Valid
      @RequestBody
      CategoryRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader,
      UriComponentsBuilder uriBuilder) throws Exception {
    var response = service.create(getUserId(authorizationHeader), request)
        .orElseThrow(() -> new Exception("Can't create category"));

    var categoryResponse = mapper.toDto(response);

    var uri = uriBuilder
        .path("/category/{id}")
        .buildAndExpand(categoryResponse.id())
        .toUri();

    return ResponseEntity
        .created(uri)
        .body(categoryResponse);
  }

  @GetMapping()
  public ResponseEntity<List<CategoryResponse>> getAllByUser(
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var response = service.findByUserId(getUserId(authorizationHeader));

    var categories = response.stream().map(mapper::toDto).toList();

    return ResponseEntity.ok(categories);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> getById(
      @PathVariable
      Long id,
      @RequestHeader("Authorization")
      String authorizationHeader) {

    var category = service.findById(id, getUserId(authorizationHeader)).orElse(null);

    if (category == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(category));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> update(
      @PathVariable
      Long id,
      @RequestBody
      CategoryRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var category = service.update(id, getUserId(authorizationHeader), request).orElse(null);

    if (category == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(category));
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
