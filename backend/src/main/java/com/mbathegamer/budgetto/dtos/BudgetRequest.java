package com.mbathegamer.budgetto.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record BudgetRequest(
    @JsonProperty("category-id")
    @NotBlank(message = "Category is required")
    long categoryId,

    @JsonProperty("amount-limit")
    @NotBlank(message = "Amount limit is required")
    double amountLimit,

    @JsonProperty("period")
    @NotBlank(message = "Period is required")
    String period,

    @JsonProperty("start-date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @JsonProperty("end-date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate,

    @JsonProperty("alert-threshold")
    byte alertThreshold
) {}
