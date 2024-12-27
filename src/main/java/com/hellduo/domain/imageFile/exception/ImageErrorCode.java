package com.hellduo.domain.imageFile.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    IMAGE_CURRENT_USER(HttpStatus.NOT_FOUND, "잘못된 접근입니다. 이미지 업로더와 로그인 정보가 일치하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
