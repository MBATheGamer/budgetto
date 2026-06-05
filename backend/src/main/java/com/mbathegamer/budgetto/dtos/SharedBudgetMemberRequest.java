package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SharedBudgetMemberRequest(
    @JsonProperty("email")
    String email,

    @JsonProperty("status")
    String status
) {}
