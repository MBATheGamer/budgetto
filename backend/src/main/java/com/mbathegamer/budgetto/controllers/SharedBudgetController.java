package com.mbathegamer.budgetto.controllers;

import java.util.ArrayList;
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

import com.mbathegamer.budgetto.dtos.SharedBudgetMemberRequest;
import com.mbathegamer.budgetto.dtos.SharedBudgetRequest;
import com.mbathegamer.budgetto.dtos.SharedBudgetResponse;
import com.mbathegamer.budgetto.entities.SharedBudgetMember;
import com.mbathegamer.budgetto.entities.SharedBudgetMemberRole;
import com.mbathegamer.budgetto.entities.SharedBudgetMemberStatus;
import com.mbathegamer.budgetto.mappers.SharedBudgetMapper;
import com.mbathegamer.budgetto.services.JwtService;
import com.mbathegamer.budgetto.services.SharedBudgetMemberService;
import com.mbathegamer.budgetto.services.SharedBudgetService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/shared-budgets")
public class SharedBudgetController {
  private final SharedBudgetService service;
  private final SharedBudgetMemberService sharedBudgetMemberService;
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
    var userId = getUserId(authorizationHeader);
    var response = service.create(userId, request)
        .orElseThrow(() -> new Exception("Can't create category"));

    var sharedBudgetMember = sharedBudgetMemberService
        .create(userId, response, SharedBudgetMemberRole.OWNER, SharedBudgetMemberStatus.ACTIVE)
        .orElse(null);

    if (sharedBudgetMember == null) {
      throw new Exception("Invalid owner");
    }

    var sharedBudgetMembers = new ArrayList<SharedBudgetMember>();
    sharedBudgetMembers.add(sharedBudgetMember);

    var owner = sharedBudgetMember.getUser().getEmail();

    var emails = request.memberMails().split(",");

    for (var email : emails) {
      if (!owner.equals(email)) {
        sharedBudgetMember = sharedBudgetMemberService.create(
            email.trim(), response, SharedBudgetMemberRole.MEMBER, SharedBudgetMemberStatus.PENDING
        ).orElse(null);

        if (sharedBudgetMember != null) {
          sharedBudgetMembers.add(sharedBudgetMember);
        }
      }
    }

    response.setMembers(sharedBudgetMembers);
    var sharedBudgetResponse = mapper.toDto(response);

    var uri = uriBuilder
        .path("/shared-budgets/{id}")
        .buildAndExpand(sharedBudgetResponse.id())
        .toUri();

    return ResponseEntity
        .created(uri)
        .body(sharedBudgetResponse);
  }

  @GetMapping()
  public ResponseEntity<List<SharedBudgetResponse>> getAllByUser(
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var response = service.findByUserId(getUserId(authorizationHeader));

    var sharedBudgets = response.stream().map(mapper::toDto).toList();

    return ResponseEntity.ok(sharedBudgets);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SharedBudgetResponse> getById(
      @PathVariable
      Long id,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var sharedBudget = service.findById(id, getUserId(authorizationHeader)).orElse(null);

    if (sharedBudget == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(sharedBudget));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SharedBudgetResponse> update(
      @PathVariable
      Long id,
      @RequestBody
      SharedBudgetRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var sharedBudget = service.update(id, getUserId(authorizationHeader), request).orElse(null);

    if (sharedBudget == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(mapper.toDto(sharedBudget));
  }

  @PostMapping("/{id}/members")
  public ResponseEntity<SharedBudgetResponse> addMember(
      @PathVariable
      Long id,
      @RequestBody
      SharedBudgetMemberRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var sharedBudget = service.findById(id, getUserId(authorizationHeader)).orElse(null);
    if (sharedBudget == null) {
      return ResponseEntity.notFound().build();
    }

    var sharedBudgetMember = sharedBudgetMemberService.create(
        request.email(),
        sharedBudget,
        SharedBudgetMemberRole.MEMBER,
        SharedBudgetMemberStatus.PENDING
    ).orElse(null);

    if (sharedBudgetMember == null) {
      return ResponseEntity.notFound().build();
    }

    sharedBudget.getMembers().add(sharedBudgetMember);

    return ResponseEntity.ok(mapper.toDto(sharedBudget));
  }

  @PostMapping("/{id}/members/{member-id}")
  public ResponseEntity<Void> updateMemberStatus(
      @PathVariable
      Long id,
      @PathVariable("member-id")
      Long memberId,
      @RequestBody
      SharedBudgetMemberRequest request,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    var statusValue = SharedBudgetMemberStatus.valueOf(request.status());

    if (statusValue != SharedBudgetMemberStatus.ACTIVE) {
      sharedBudgetMemberService.delete(id, memberId);
      return ResponseEntity.ok().build();
    }

    var userId = getUserId(authorizationHeader);
    var sharedBudget = service.findById(id, userId).orElse(null);

    if (sharedBudget == null) {
      return ResponseEntity.notFound().build();
    }

    var sharedBudgetMember = sharedBudgetMemberService
        .findBySharedBudgetIdAndUserId(memberId, sharedBudget.getId(), userId)
        .orElse(null);

    if (sharedBudgetMember == null) {
      return ResponseEntity.notFound().build();
    }

    sharedBudgetMember = sharedBudgetMemberService
        .update(memberId, userId, SharedBudgetMemberStatus.ACTIVE)
        .orElse(sharedBudgetMember);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public void delete(
      @PathVariable
      Long id,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    service.delete(id, getUserId(authorizationHeader));
  }

  @DeleteMapping("/{id}/members/{member-id}")
  public void removeMember(
      @PathVariable
      Long id,
      @PathVariable("member-id")
      Long memberId,
      @RequestHeader("Authorization")
      String authorizationHeader) {
    sharedBudgetMemberService.delete(memberId, getUserId(authorizationHeader));
  }

  private Long getUserId(String authorizationHeader) {
    var token = authorizationHeader.replace("Bearer ", "");
    return jwtService.getUserIdFromToken(token);
  }
}
