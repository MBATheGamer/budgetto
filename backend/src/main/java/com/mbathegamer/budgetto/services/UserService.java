package com.mbathegamer.budgetto.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.RegisterUserRequest;
import com.mbathegamer.budgetto.entities.User;
import com.mbathegamer.budgetto.entities.UserRole;
import com.mbathegamer.budgetto.entities.UserStatus;
import com.mbathegamer.budgetto.mappers.UserMapper;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;
  private final UserMapper mapper;

  public Optional<User> register(RegisterUserRequest request) {
    if (repository.existsByEmail(request.email().toLowerCase())) {
      return Optional.empty();
    }

    var user = mapper.toEntity(request);
    user.setEmail(user.getEmail().toLowerCase());
    user.setRole(UserRole.USER);
    user.setStatus(UserStatus.PENDING);
    repository.save(user);

    return Optional.of(user);
  }
}
