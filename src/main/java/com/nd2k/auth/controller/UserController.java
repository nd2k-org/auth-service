package com.nd2k.auth.controller;

import com.nd2k.auth.models.User;
import com.nd2k.auth.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/v1")
@Slf4j
public record UserController(UserService userService) {

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<String> userController(@RequestBody String userRequest) {
        log.info("User Controller reached");
        log.info(userRequest);
        return ResponseEntity.ok(userRequest);
    }
}
