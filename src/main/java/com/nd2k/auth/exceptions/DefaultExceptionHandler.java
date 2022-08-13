package com.nd2k.auth.exceptions;

import com.nd2k.auth.models.dto.responses.RestErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ BadCredentialsException.class })
    @ResponseBody
    public ResponseEntity<RestErrorResponse> handleBadCredentialsException() {
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestErrorResponse restErrorResponse = new RestErrorResponse(HttpStatus.BAD_REQUEST,
                "Bad Credentials");
        return new ResponseEntity<>(restErrorResponse, httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<RestErrorResponse> handleAuthenticationException() {
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestErrorResponse restErrorResponse = new RestErrorResponse(HttpStatus.UNAUTHORIZED,
                "Authentication failed");
        return new ResponseEntity<>(restErrorResponse, httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    public ResponseEntity<RestErrorResponse> handleAuthorizationException() {
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestErrorResponse restErrorResponse = new RestErrorResponse(HttpStatus.UNAUTHORIZED,
                "Authorization failed");
        return new ResponseEntity<>(restErrorResponse, httpHeaders, HttpStatus.UNAUTHORIZED);
    }


}
