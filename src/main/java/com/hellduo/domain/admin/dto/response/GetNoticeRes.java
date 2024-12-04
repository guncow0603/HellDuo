package com.hellduo.domain.admin.dto.response;

public record GetNoticeRes(
        String title,
        String content,
        Long userId,
        Long noticeId
) {
}
