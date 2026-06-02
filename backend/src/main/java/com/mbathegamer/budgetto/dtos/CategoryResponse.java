package com.mbathegamer.budgetto.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryResponse(
    @JsonProperty("id")
    Long id,

    @JsonProperty("username")
    String username,

    @JsonProperty("name")
    String name,

    @JsonProperty("icon")
    String icon,

    @JsonProperty("type")
    String type,

    @JsonProperty("is-default")
    boolean isDefault
) {}
