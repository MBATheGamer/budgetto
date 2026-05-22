package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
    @JsonProperty("first-name")
    @NotBlank(message = "First name is required")
    @Size(max = 64, message = "Name must be less then 64 characters")
    String firstName,

    @JsonProperty("last-name")
    @NotBlank(message = "Last name is required")
    @Size(max = 64, message = "Name must be less then 64 characters")
    String lastName,

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Password must be between 8 to 32 characters long")
    String password
) {}
