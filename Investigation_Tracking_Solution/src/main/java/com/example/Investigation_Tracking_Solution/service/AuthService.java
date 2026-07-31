package com.example.Investigation_Tracking_Solution.service;

import com.example.Investigation_Tracking_Solution.annotation.Auditable;
import com.example.Investigation_Tracking_Solution.dto.AuthRequest;
import com.example.Investigation_Tracking_Solution.dto.AuthResponse;
import com.example.Investigation_Tracking_Solution.dto.RegisterRequest;
import com.example.Investigation_Tracking_Solution.model.AuditAction;
import com.example.Investigation_Tracking_Solution.model.AuditModule;
import com.example.Investigation_Tracking_Solution.model.User;
import com.example.Investigation_Tracking_Solution.repository.UserRepo;
import com.example.Investigation_Tracking_Solution.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Auditable(action = AuditAction.CREATE, module = AuditModule.AUTH, entityType = "USER", description = "User registration")
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();

        userRepo.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getRole());
    }

    @Auditable(action = AuditAction.LOGIN, module = AuditModule.AUTH, entityType = "USER", description = "User login")
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getRole());
    }
}
