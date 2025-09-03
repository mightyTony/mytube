package com.example.mytube.auth.dto;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        String role
) {
}
