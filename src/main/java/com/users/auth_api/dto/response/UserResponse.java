package com.users.auth_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    @JsonProperty("id_user") Long idUser
) {
}
