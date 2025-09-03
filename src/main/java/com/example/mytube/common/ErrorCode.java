package com.example.mytube.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_ALREADY_EXISTS(400, "USER_ALREADY_EXISTS", "이미 존재하는 사용자입니다."),
    USER_NOT_FOUND(-1, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(400, "INVALID_PASSWORD","비밀번호가 일치하지 않습니다." ),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "내부 서버 오류입니다."),
    INVALID_TOKEN(401,"INVALID_TOKEN" ,"유효하지 않은 토큰 입니다." );


    private final int status;
    private final String code;
    private final String description;
}
