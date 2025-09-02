package com.example.mytube.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class SignUpRequestDTO {
    private String username;
    private String password;
    private String email;
}
