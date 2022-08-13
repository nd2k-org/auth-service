package com.nd2k.auth.models.dto.requests;

import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
public record SignUpRequest(
        @NotBlank
        @Size(min = 4, max = 100)
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 120)
        String password
) {
}
