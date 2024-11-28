package com.hellduo.domain.payment.dto.request;

import java.time.LocalDateTime;

public record PaymentToApiRequest(
        Long amount,
        String orderName,
        String orderId,
        String userEmail,
        String userName,
        String successUrl,
        String failUrl,
        LocalDateTime createdAt
) { }
