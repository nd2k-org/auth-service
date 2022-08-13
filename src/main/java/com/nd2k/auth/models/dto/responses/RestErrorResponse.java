package com.nd2k.auth.models.dto.responses;

import org.springframework.http.HttpStatus;

public record RestErrorResponse(HttpStatus httpStatus, String errorMessage) {
}
