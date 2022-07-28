package com.nd2k.auth.models.dto;

import lombok.Builder;

@Builder
public record SignUpRequest(
        String email,
        String password
) {
}
