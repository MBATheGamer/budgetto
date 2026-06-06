package com.mbathegamer.budgetto.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SharedBudgetRequest(
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    String name,

    @DecimalMin("0.00")
    @JsonProperty("amount-limit")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "Amount limit is required")
    Double amountLimit,

    @JsonProperty("period-type")
    @NotBlank(message = "Period type is required")
    String periodType,

    @JsonProperty("start-date")
    @NotNull(message = "Start date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @JsonProperty("end-date")
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate,

    @JsonProperty("members-mail")
    String memberMails
) {}
