package com.hellduo.domain.payment.controller;

import com.hellduo.domain.payment.dto.request.PaymentFromUserRequest;
import com.hellduo.domain.payment.dto.request.PaymentToApiRequest;
import com.hellduo.domain.payment.dto.response.PaymentSuccessResponse;
import com.hellduo.domain.payment.service.PaymentService;
import com.hellduo.domain.user.entity.User;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> requestPayment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid PaymentFromUserRequest request
    ) {
        Map<String, Object> status = new HashMap<>();
        PaymentToApiRequest toApiRequest = paymentService.requestPayment(userDetails.getUser().getId(), request);
        status.put("request", toApiRequest);

        return ResponseEntity.ok(status);
    }

    @PostMapping("/success")
    public ResponseEntity<?> successPayment(@RequestBody String jsonBody) throws Exception {
        PaymentSuccessResponse response = paymentService.successPayment(jsonBody);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getKey")
    public ResponseEntity<?> getKey( @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(paymentService.getKey());
    }

}