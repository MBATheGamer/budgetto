package com.mbathegamer.budgetto.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

  private Long getUserId(String authorizationHeader) {
    var token = authorizationHeader.replace("Bearer ", "");
    return jwtService.getUserIdFromToken(token);
  }
}
