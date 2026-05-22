package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    @JsonProperty("user-id")
    Long id,

    @JsonProperty("first-name")
    String firstName,

    @JsonProperty("last-name")
    String lastName,

    String email
) {}
