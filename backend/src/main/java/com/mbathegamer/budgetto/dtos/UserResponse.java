package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    @JsonIgnore
    Long id,

    @JsonProperty("first-name")
    String firstName,

    @JsonProperty("last-name")
    String lastName,

    String email
) {}
