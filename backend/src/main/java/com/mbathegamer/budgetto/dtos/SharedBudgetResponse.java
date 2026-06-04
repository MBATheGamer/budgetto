package com.mbathegamer.budgetto.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SharedBudgetResponse(
    @JsonProperty("id")
    Long id,

    @JsonProperty("name")
    String name,

    @JsonProperty("amount-limit")
    Double amountLimit,

    @JsonProperty("period-type")
    String periodType,

    @JsonProperty("start-date")
    LocalDate startDate,

    @JsonProperty("end-date")
    LocalDate endDate
) {}
