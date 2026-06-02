package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    String name,

    @JsonProperty("icon")
    String icon,

    @JsonProperty("type")
    @NotBlank(message = "Type is required")
    String type
) {}
