package com.hellduo.domain.admin.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportReason {
    INAPPROPRIATE_CONTENT("부적절한 내용입니다."),
    ABUSE("욕설 및 비방입니다."),
    FRAUD("사기 행위입니다.");
    private final String reportReason;
}

