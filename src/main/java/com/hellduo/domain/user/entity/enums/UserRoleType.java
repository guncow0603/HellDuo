package com.hellduo.domain.user.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    TRAINER("ROLE_TRAINER");

    private final String authority;
}
