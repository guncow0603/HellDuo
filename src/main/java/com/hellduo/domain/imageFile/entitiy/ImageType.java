package com.hellduo.domain.imageFile.entitiy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    PROFILE_IMG("프로필"),
    CERTS_IMG("자격증");

    private final String type;
}
