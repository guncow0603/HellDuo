package com.hellduo.domain.payment.dto.request;

public record PaymentFromUserRequest(
        Long amount,
        String orderName
) {
}
