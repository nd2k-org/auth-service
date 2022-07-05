package com.nd2k.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/v1")
@Slf4j
public record TestController() {

    @GetMapping
    public ResponseEntity<String> testController() {
        log.info("Test Controller is working!!!!");
        return ResponseEntity.ok("Test Controller is working!!!!");
    }
}
