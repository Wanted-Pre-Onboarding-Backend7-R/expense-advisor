package com.example.expenseadvisor.auth.controller;

import com.example.expenseadvisor.auth.dto.LoginRequest;
import com.example.expenseadvisor.auth.dto.LoginResponse;
import com.example.expenseadvisor.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        String jwt = authService.login(loginRequest);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

}
