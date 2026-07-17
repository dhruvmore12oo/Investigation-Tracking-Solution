package com.example.Investigation_Tracking_Solution.controller;

import com.example.Investigation_Tracking_Solution.dto.AuthRequest;
import com.example.Investigation_Tracking_Solution.dto.AuthResponse;
import com.example.Investigation_Tracking_Solution.dto.RegisterRequest;
import com.example.Investigation_Tracking_Solution.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse register(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
