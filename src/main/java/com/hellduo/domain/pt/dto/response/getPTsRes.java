package com.hellduo.domain.pt.dto.response;

import java.time.LocalDateTime;

public record getPTsRes(
        Long ptId,
        String title,
        String address
) {
}
