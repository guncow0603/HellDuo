package com.hellduo.domain.imageFile.entitiy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    PROFILE_IMG("프로필"),
    CERTS_IMG("자격증"),
    THUMBNAIL("썸네일"),
    REGULAR("일반");
    private final String type;
}
