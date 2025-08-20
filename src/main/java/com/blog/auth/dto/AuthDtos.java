package com.blog.auth.dto;

import jakarta.validation.constraints.*;

public class AuthDtos{
    public record RegisterRequest (
            @NotBlank
            String username,

            @Email
            @NotBlank
            String email,

            @Size(min = 6, max = 20)
            String password
    ){}

    public record LoginRequest (
            @NotBlank
            String username,
            @NotBlank
            String password
    ){}
    public record JwtResponse(String token, String username){}
}
