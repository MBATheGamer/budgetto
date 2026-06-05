package com.mbathegamer.budgetto.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.entities.SharedBudget;
import com.mbathegamer.budgetto.entities.SharedBudgetMember;
import com.mbathegamer.budgetto.entities.SharedBudgetMemberRole;
import com.mbathegamer.budgetto.entities.SharedBudgetMemberStatus;
import com.mbathegamer.budgetto.entities.User;
import com.mbathegamer.budgetto.mappers.SharedBudgetMemberMapper;
import com.mbathegamer.budgetto.repositories.SharedBudgetMemberRepository;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SharedBudgetMemberService {
  private final UserRepository userRepository;
  private final SharedBudgetMemberRepository sharedBudgetMemberRepository;
  private final SharedBudgetMemberMapper mapper;

  public Optional<SharedBudgetMember> save(User user, SharedBudget sharedBudget,
      SharedBudgetMemberRole role, SharedBudgetMemberStatus status) {
    if (user == null) {
      return Optional.empty();
    }

    var member = mapper.toEntity(user, sharedBudget, role, status);

    sharedBudgetMemberRepository.save(member);

    return Optional.of(member);
  }

  public Optional<SharedBudgetMember> create(Long userId, SharedBudget sharedBudget,
      SharedBudgetMemberRole role, SharedBudgetMemberStatus status) {
    return save(
        userRepository.findById(userId).orElse(null),
        sharedBudget,
        role,
        status
    );
  }

  public Optional<SharedBudgetMember> create(String email, SharedBudget sharedBudget,
      SharedBudgetMemberRole role, SharedBudgetMemberStatus status) {
    return save(
        userRepository.findByEmail(email).orElse(null),
        sharedBudget,
        role,
        status
    );
  }

  public List<SharedBudgetMember> findByUserId(Long userId) {
    return sharedBudgetMemberRepository.findByUserId(userId);
  }

  public Optional<SharedBudgetMember> findById(Long id, Long userId) {
    var member = sharedBudgetMemberRepository.findById(id).orElse(null);

    if (member == null || (member.getUser() != null && member.getUser().getId() != userId)) {
      return Optional.empty();
    }

    return Optional.of(member);
  }

  public Optional<SharedBudgetMember> findBySharedBudgetIdAndUserId(Long id, Long sharedBudgetId,
      Long userId) {
    return sharedBudgetMemberRepository.findBySharedBudgetIdAndUserId(id, sharedBudgetId, userId);
  }

  public Optional<SharedBudgetMember> update(Long id, Long userId,
      SharedBudgetMemberStatus status) {
    var member = sharedBudgetMemberRepository.findById(id).orElse(null);

    if (member == null || member.getUser() == null || member.getUser().getId() != userId) {
      return Optional.empty();
    }

    member.setStatus(status);

    return Optional.of(sharedBudgetMemberRepository.save(member));
  }

  public void delete(Long id, Long userId) {
    var member = sharedBudgetMemberRepository.findById(id).orElse(null);
    var user = member.getSharedBudget().getCreatedBy();

    if (member != null && user != null && user.getId() == userId) {
      sharedBudgetMemberRepository.delete(member);
    }
  }
}
