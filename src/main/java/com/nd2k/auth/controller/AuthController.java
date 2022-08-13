package com.nd2k.auth.controller;

import com.nd2k.auth.models.domain.User;
import com.nd2k.auth.models.dto.requests.SignInRequest;
import com.nd2k.auth.models.dto.requests.SignUpRequest;
import com.nd2k.auth.models.dto.responses.TokenResponse;
import com.nd2k.auth.security.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping(
        value = "/api/auth/v1",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public record AuthController(
        UserDetailsManager userDetailsManager,
        TokenGenerator tokenGenerator,
        DaoAuthenticationProvider daoAuthenticationProvider,
        @Qualifier("jwtRefreshTokenAuthProvider")
        JwtAuthenticationProvider jwtRefreshTokenAuthProvider
) {
    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = new User(null, signUpRequest.email(), signUpRequest.password(), Collections.emptyList());
        userDetailsManager.createUser(user);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                user, signUpRequest.password(), Collections.emptyList()
        );
        return ResponseEntity.ok(tokenGenerator.createTokenResponse(authentication));
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = daoAuthenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.authenticated(
                        signInRequest.email(), signInRequest.password(), Collections.emptyList())
        );
        return ResponseEntity.ok(tokenGenerator.createTokenResponse(authentication));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> getTokenBasedOnRefreshToken(@RequestBody TokenResponse tokenResponse) {
        Authentication authentication = jwtRefreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenResponse.refreshToken()));
        Jwt jwt = (Jwt) authentication.getCredentials();
        // TODO: check if the token is not revoked

        return ResponseEntity.ok(tokenGenerator.createTokenResponse(authentication));
    }
}
