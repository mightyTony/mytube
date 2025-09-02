package com.example.mytube.auth.service;

import com.example.mytube.auth.dto.SignUpRequestDTO;
import com.example.mytube.auth.model.Role;
import com.example.mytube.auth.model.User;
import com.example.mytube.auth.repository.UserRepository;
import com.example.mytube.common.CustomException;
import com.example.mytube.common.ErrorCode;
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
}
