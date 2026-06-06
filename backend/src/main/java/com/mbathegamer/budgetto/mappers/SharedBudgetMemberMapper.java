package com.mbathegamer.budgetto.mappers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mbathegamer.budgetto.dtos.SharedBudgetMemberResponse;
import com.mbathegamer.budgetto.entities.SharedBudget;
import com.mbathegamer.budgetto.entities.SharedBudgetMember;
import com.mbathegamer.budgetto.entities.SharedBudgetMemberRole;
import com.mbathegamer.budgetto.entities.SharedBudgetMemberStatus;
import com.mbathegamer.budgetto.entities.User;

@Component
public class SharedBudgetMemberMapper {
  public SharedBudgetMemberResponse toDto(SharedBudgetMember sharedBudget) {
    var user = sharedBudget.getUser();

    return new SharedBudgetMemberResponse(
        sharedBudget.getId(),
        user.getFirstName(),
        user.getLastName(),
        sharedBudget.getRole().name(),
        sharedBudget.getStatus().name()
    );
  }

  public SharedBudgetMember toEntity(User user,
      SharedBudget sharedBudget, SharedBudgetMemberRole role, SharedBudgetMemberStatus status) {
    return SharedBudgetMember.builder()
        .user(user)
        .sharedBudget(sharedBudget)
        .role(role)
        .status(status)
        .joinedAt(LocalDateTime.now())
        .build();
  }
}
