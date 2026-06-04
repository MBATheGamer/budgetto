package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BudgetResponse(
    @JsonProperty("id")
    long id,

    @JsonProperty("category")
    String category,

    @JsonProperty("period")
    String period,

    @JsonProperty("amount-limit")
    double amountLimit
) {}
