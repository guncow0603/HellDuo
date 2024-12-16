package com.hellduo.domain.admin.dto.response;

import com.hellduo.domain.admin.entity.enums.ReportReason;

public record GetReportListRes(
        String reporterEmail,
        String reportedEmail,
        ReportReason reportReason,
        String content,
        Long reportedId
) {
}
