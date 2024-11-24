package com.hellduo.domain.pt.dto.request;

import java.time.LocalDateTime;

public record PTCreateReq(
        LocalDateTime scheduledDate,
        int price,
        String description
) {
}
