package com.hellduo.domain.imageFile.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    PROFILE_IMG("프로필"),
    CERTS_IMG("자격증"),
    PT_IMG("PT"),
    BANNER_IMG("배너"),
    BOARD_IMG("게시판"),
    REVIEW_IMG("리뷰");

    private final String type;
}
