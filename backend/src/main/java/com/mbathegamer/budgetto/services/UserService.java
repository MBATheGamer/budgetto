package com.mbathegamer.budgetto.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.entities.User;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;

  public Optional<User> register(User request) {
    if (repository.existsByEmail(request.getEmail().toLowerCase())) {
      return Optional.empty();
    }

    var user = repository.save(request);

    return user != null ? Optional.of(user) : Optional.empty();
  }
}
