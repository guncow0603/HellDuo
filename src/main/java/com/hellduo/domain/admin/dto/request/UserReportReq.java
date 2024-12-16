package com.hellduo.domain.admin.dto.request;

import com.hellduo.domain.admin.entity.enums.ReportReason;

public record UserReportReq(
        Long reportedUserId,
        ReportReason reportReason,
        String content

) {

}
