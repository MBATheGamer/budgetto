package com.mbathegamer.budgetto.mappers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mbathegamer.budgetto.dtos.RegisterRequest;
import com.mbathegamer.budgetto.dtos.UserResponse;
import com.mbathegamer.budgetto.entities.User;
import com.mbathegamer.budgetto.entities.UserRole;
import com.mbathegamer.budgetto.entities.UserStatus;

@Component
public class UserMapper {
  public UserResponse toDto(User user) {
    return new UserResponse(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail()
    );
  }

  public User toEntity(RegisterRequest request) {
    return User.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .password(request.password())
        .role(UserRole.USER)
        .status(UserStatus.PENDING)
        .failedAttempts((byte) 0)
        .deletionRequested(false)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
