package com.example.Investigation_Tracking_Solution.dto;

import com.example.Investigation_Tracking_Solution.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
//    private String refreshToken;
    private Role role;
}
