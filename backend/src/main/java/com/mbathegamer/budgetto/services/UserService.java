package com.mbathegamer.budgetto.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.RegisterRequest;
import com.mbathegamer.budgetto.entities.User;
import com.mbathegamer.budgetto.entities.UserRole;
import com.mbathegamer.budgetto.entities.UserStatus;
import com.mbathegamer.budgetto.mappers.UserMapper;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository repository;
  private final UserMapper mapper;

  public Optional<User> register(RegisterRequest request) {
    if (repository.existsByEmail(request.email().toLowerCase())) {
      return Optional.empty();
    }

    var encoder = new BCryptPasswordEncoder();

    var user = mapper.toEntity(request);
    user.setPassword(encoder.encode(user.getPassword()));
    user.setEmail(user.getEmail().toLowerCase());
    user.setRole(UserRole.USER);
    user.setStatus(UserStatus.PENDING);
    repository.save(user);

    return Optional.of(user);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return repository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
