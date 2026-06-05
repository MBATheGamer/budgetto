package com.mbathegamer.budgetto.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionResponse(
    @JsonProperty("id")
    Long id,

    @JsonProperty("transaction-date")
    LocalDate transactionDate,

    @JsonProperty("description")
    String description,

    @JsonProperty("category")
    String category,

    @JsonProperty("author-first-name")
    String authorFirstName,

    @JsonProperty("author-last-name")
    String authorLastName,

    @JsonProperty("type")
    String type,

    @JsonProperty("amount")
    Double amount
) {}
