package com.example.mytube.auth.service;

import com.example.mytube.auth.dto.LoginRequestDTO;
import com.example.mytube.auth.dto.LoginResponseDTO;
import com.example.mytube.auth.dto.SignUpRequestDTO;
import com.example.mytube.auth.dto.UserResponseDTO;
import com.example.mytube.auth.model.Role;
import com.example.mytube.auth.model.User;
import com.example.mytube.auth.repository.UserRepository;
import com.example.mytube.common.CustomException;
import com.example.mytube.common.ErrorCode;
import com.example.mytube.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(SignUpRequestDTO requestDTO) {
        if(userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            // 이미 존재하는 사용자
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .email(requestDTO.getEmail())
                .role(Role.valueOf("ROLE_USER"))
                .build();

        userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO requestDTO) {

        User user = userRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(requestDTO.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(requestDTO.getUsername());

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public UserResponseDTO getMyInfo(String accessToken) {
        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String userId = jwtTokenProvider.getUsernameFromToken(accessToken);
        User user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name());
    }

    @Transactional
    public void logout(String accessToken, HttpServletResponse response) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtTokenProvider.getUsernameFromToken(accessToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // DB Refresh Token 제거
        user.updateRefreshToken(null);

        // 쿠키 제거
        Cookie deleteCookie = new Cookie("refreshToken", null);
        deleteCookie.setHttpOnly(true);
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0);
        response.addCookie(deleteCookie);

        log.info("[LOGOUT] 로그아웃 처리 완료 - username : {}", username);
    }
}
