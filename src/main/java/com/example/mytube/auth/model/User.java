package com.example.mytube.auth.model;

import com.example.mytube.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username; // 아이디
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 권한
    @Column(nullable = false, unique = true)
    private String email;
    private String provider; // LOCAL, GOOGLE, NAVER, KAKAO
    private String providerId; // OAuth2 제공자에서 제공하는 고유 ID
    @Column(name = "refresh_token", length = 200)
    private String refreshToken;

    @Builder
    public User(String username, String password, Role role, String email, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
