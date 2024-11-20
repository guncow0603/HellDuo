package com.hellduo.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Specialization {
    FITNESS("피트니스"),
    YOGA("요가"),
    PILATES("필라테스"),
    NUTRITION("영양학"),
    REHABILITATION("재활운동"),
    CROSSFIT("크로스핏");

    private final String authority;
}
