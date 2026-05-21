package com.mbathegamer.budgetto.services;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.LoginRequest;
import com.mbathegamer.budgetto.dtos.RegisterRequest;
import com.mbathegamer.budgetto.dtos.UserResponse;
import com.mbathegamer.budgetto.entities.User;
import com.mbathegamer.budgetto.entities.UserRole;
import com.mbathegamer.budgetto.entities.UserStatus;
import com.mbathegamer.budgetto.exceptions.InvalidCredentialsException;
import com.mbathegamer.budgetto.mappers.UserMapper;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;
  private final UserMapper mapper;
  private final PasswordEncoder encoder = new BCryptPasswordEncoder();

  public Optional<User> register(RegisterRequest request) {
    if (repository.existsByEmail(request.email().toLowerCase())) {
      return Optional.empty();
    }

    var user = mapper.toEntity(request);
    user.setPassword(encoder.encode(user.getPassword()));
    user.setEmail(user.getEmail().toLowerCase());
    user.setRole(UserRole.USER);
    user.setStatus(UserStatus.PENDING);
    repository.save(user);

    return Optional.of(user);
  }

  public UserResponse login(LoginRequest request) {
    var user = repository.findByEmail(request.email()).orElse(null);

    if (user == null || !encoder.matches(request.password(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    return mapper.toDto(user);
  }
}
