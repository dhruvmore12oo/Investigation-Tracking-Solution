package com.example.Investigation_Tracking_Solution.dto;


import com.example.Investigation_Tracking_Solution.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
