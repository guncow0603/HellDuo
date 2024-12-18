package com.hellduo.domain.user.dto.response;

import java.time.LocalDateTime;

public record ChargePointRes(
        Long id,
        LocalDateTime time,
        Long amount,
        String chargeEmail
) { }
