package com.nd2k.auth.models.dto.responses;

public record TokenResponse(
        String userEmail,
        String accessToken,
        String refreshToken
) {
}
