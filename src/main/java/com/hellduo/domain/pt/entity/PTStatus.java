package com.hellduo.domain.pt.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PTStatus {
    SCHEDULED("예약됨"),
    COMPLETED("완료됨"),
    CANCELED("취소됨"),
    WAITING("대기 중");

    private final String description;
}