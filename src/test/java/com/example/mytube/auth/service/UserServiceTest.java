package com.example.mytube.auth.service;

import com.example.mytube.auth.dto.SignUpRequestDTO;
import com.example.mytube.auth.model.Role;
import com.example.mytube.auth.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("회원가입 테스트")
    @Rollback(value = true)
    void signUp() {
        SignUpRequestDTO requestDTO = SignUpRequestDTO.builder()
                .username("testuser")
                .password("password")
                .email("testuser@gmail.com")
                .build();

        userService.signUp(requestDTO);

        assertEquals("testuser", requestDTO.getUsername());
        assertTrue(passwordEncoder.matches("password", passwordEncoder.encode(requestDTO.getPassword())));
        assertEquals("testuser@gmail.com", requestDTO.getEmail());
    }
}