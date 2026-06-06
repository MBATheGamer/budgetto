package com.mbathegamer.budgetto.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
    @JsonProperty("type")
    @NotBlank(message = "Type is required")
    String type,

    @JsonProperty("description")
    @NotBlank(message = "Description is required")
    String description,

    @DecimalMin("0.00")
    @JsonProperty("amount")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "Amount is required")
    BigDecimal amount,

    @JsonProperty("transaction-date")
    @NotNull(message = "Transaction date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate transactionDate,

    @JsonProperty("category-id")
    @NotNull(message = "Category is required")
    Long categoryId,

    @JsonProperty("shared-budget-id")
    Long sharedBudgetId,

    @JsonProperty("comment")
    String comment
) {}
