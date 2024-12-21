package com.hellduo.domain.admin.dto.response;

import com.hellduo.domain.admin.entity.enums.ReportReason;
import com.hellduo.domain.user.entity.enums.UserStatus;

public record GetReportListRes(
        String reporterEmail,
        String reportedEmail,
        String reportReason,
        String content,
        Long reportedId,
        UserStatus userStatus
) {
}
