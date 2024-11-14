package com.hellduo.domain.user.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "해당 리프레시 토큰을 찾을 수 없습니다."),
    BAD_LOGIN(HttpStatus.BAD_REQUEST, "패스워드를 확인해주세요."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    INVALID_PASSWORD_CHECK(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    INVALID_ADMIN_CODE(HttpStatus.BAD_REQUEST, "관리자 인증 번호가 틀렸습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 가입된 닉네임 입니다."),
    ALREADY_EXIST_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 가입된 전화번호 입니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
