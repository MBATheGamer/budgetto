package com.mbathegamer.budgetto.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @Column(name = "failed_attempts")
  private byte failedAttempts;

  @Column(name = "locked_until")
  private LocalDateTime lockedUntil;

  @Column(name = "deletion_requested")
  private boolean deletionRequested;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;
}
