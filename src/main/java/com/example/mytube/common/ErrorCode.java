package com.example.mytube.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_ALREADY_EXISTS(400, "USER_ALREADY_EXISTS", "이미 존재하는 사용자입니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "내부 서버 오류입니다.");


    private final int status;
    private final String code;
    private final String description;
}
