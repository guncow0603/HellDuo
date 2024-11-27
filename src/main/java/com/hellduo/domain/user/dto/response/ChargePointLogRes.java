package com.hellduo.domain.user.dto.response;

import java.time.LocalDateTime;

public record ChargePointLogRes(
        Long id,
        LocalDateTime time,
        Long amount,
        String orderId
) { }
