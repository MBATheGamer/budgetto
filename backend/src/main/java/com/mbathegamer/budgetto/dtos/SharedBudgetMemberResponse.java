package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SharedBudgetMemberResponse(
    @JsonProperty("id")
    Long id,

    @JsonProperty("first-name")
    String firstName,

    @JsonProperty("last-name")
    String lastName,

    @JsonProperty("role")
    String role,

    @JsonProperty("status")
    String status
) {}
