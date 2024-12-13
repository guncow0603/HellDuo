package com.hellduo.domain.pt.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum PTStatus {
    SCHEDULED("예약됨"),
    COMPLETED("완료됨"),
    CANCELED("취소됨"),
    UNRESERVED("미예약");

    private final String description;
}