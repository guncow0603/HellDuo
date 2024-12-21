package com.hellduo.domain.user.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum UserStatus {
    DELETED("탈퇴계정"),
    REST("휴먼계정"),
    ACTION("활동계정");
    private final String userStatus;
}
