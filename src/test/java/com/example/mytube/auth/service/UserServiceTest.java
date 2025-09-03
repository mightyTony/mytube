package com.example.mytube.auth.service;

import com.example.mytube.auth.dto.LoginRequestDTO;
import com.example.mytube.auth.dto.LoginResponseDTO;
import com.example.mytube.auth.dto.SignUpRequestDTO;
import com.example.mytube.auth.model.Role;
import com.example.mytube.auth.model.User;
import com.example.mytube.auth.repository.UserRepository;
import com.example.mytube.common.CustomException;
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
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("test")
                .password(passwordEncoder.encode("TEST"))
                .email("test@gmail.com")
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
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

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        SignUpRequestDTO signrequestDTO = SignUpRequestDTO.builder()
                .username("testuser")
                .password("password")
                .email("testuser@gmail.com")
                .build();

        userService.signUp(signrequestDTO);

        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .username("testuser")
                .password("password")
                .build();

        LoginResponseDTO login = userService.login(requestDTO);

        assertNotNull(login.getAccessToken());
        assertNotNull(login.getRefreshToken());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 사용자 없음")
    void loginFailUserNotFound() {
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .username("nonexistentuser")
                .password("password")
                .build();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.login(requestDTO);
        });

        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void login_invalid_password() {
        SignUpRequestDTO signrequestDTO = SignUpRequestDTO.builder()
                .username("testuser")
                .password("password")
                .email("testuser@gmail.com")
                .build();

        userService.signUp(signrequestDTO);

        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

        assertThrows(CustomException.class, () -> {
            userService.login(requestDTO);
        });
    }

}