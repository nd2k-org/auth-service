package com.nd2k.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/v1")
@Slf4j
public record UserController() {

    @PostMapping
    public ResponseEntity<String> userController(@RequestBody String userRequest) {
        log.info("User Controller reached");
        log.info(userRequest);
        return ResponseEntity.ok(userRequest);
    }
}
