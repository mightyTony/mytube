package com.example.mytube.auth.controller;

import com.example.mytube.auth.dto.SignUpRequestDTO;
import com.example.mytube.auth.service.UserService;
import com.example.mytube.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@RequestBody SignUpRequestDTO requestDTO) {
        userService.signUp(requestDTO);
        return ApiResponse.success(201, "User registered successfully", null);
    }
}
