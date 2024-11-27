package com.hellduo.domain.imageFile.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    NOT_FOUND_PROFILE(HttpStatus.NOT_FOUND, "프로필 이미지를 찾을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
