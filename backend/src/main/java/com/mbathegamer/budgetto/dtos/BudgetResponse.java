package com.mbathegamer.budgetto.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BudgetResponse(
    @JsonProperty("id")
    Long id,

    @JsonProperty("category-id")
    Long categoryId,

    @JsonProperty("category-name")
    String categoryName,

    @JsonProperty("period")
    String period,

    @JsonProperty("amount-limit")
    Double amountLimit,

    @JsonProperty("start-date")
    LocalDate startDate,

    @JsonProperty("end-date")
    LocalDate endDate,

    @JsonProperty("alert-threshold")
    Byte alertThreshold
) {}
