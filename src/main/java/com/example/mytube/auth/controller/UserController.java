package com.example.mytube.auth.controller;

import com.example.mytube.auth.dto.LoginRequestDTO;
import com.example.mytube.auth.dto.LoginResponseDTO;
import com.example.mytube.auth.dto.SignUpRequestDTO;
import com.example.mytube.auth.dto.UserResponseDTO;
import com.example.mytube.auth.model.User;
import com.example.mytube.auth.repository.UserRepository;
import com.example.mytube.auth.service.UserService;
import com.example.mytube.common.ApiResponse;
import com.example.mytube.common.CustomException;
import com.example.mytube.common.ErrorCode;
import com.example.mytube.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@RequestBody SignUpRequestDTO requestDTO) {
        userService.signUp(requestDTO);
        return ApiResponse.success(201, "회원가입 성공", null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) {
        LoginResponseDTO tokens = userService.login(requestDTO);

//        response.addHeader("Authorization", "Bearer " + tokens.getAccessToken());

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(refreshTokenCookie);

        return ApiResponse.success(200, "로그인 성공", new LoginResponseDTO(tokens.getAccessToken(), null));
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refresh(HttpServletRequest request) {
        // 쿠키에서 Refresh Token 추출
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ApiResponse.success(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 리프레시 토큰입니다.", null);
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!refreshToken.equals(user.getRefreshToken())) {
            return ApiResponse.success(HttpStatus.UNAUTHORIZED.value(), "서버에 저장된 리프레시 토큰과 다릅니다.", null);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(username, user.getRole().name());

        return ApiResponse.success(200, "토큰 재발급 성공", new LoginResponseDTO(newAccessToken, null));
    }

    //- GET /api/v1/auth/me : 내 정보 조회
    @GetMapping("/me")
    public ApiResponse<UserResponseDTO> getMyInfo(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.replace("Bearer ", ""); // Assign the modified string back
        }

        UserResponseDTO response = userService.getMyInfo(accessToken);

        return ApiResponse.success(200, "내 정보 조회 성공", response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = extractToken(request);
        userService.logout(accessToken, response);

        return ApiResponse.success(200, "로그아웃 성공", null);
    }
    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new CustomException(ErrorCode.INVALID_TOKEN);
    }
}
