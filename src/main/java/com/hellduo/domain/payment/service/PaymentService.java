package com.hellduo.domain.payment.service;
import com.hellduo.domain.payment.dto.request.PaymentFromUserRequest;
import com.hellduo.domain.payment.dto.request.PaymentToApiRequest;
import com.hellduo.domain.payment.dto.response.PaymentSuccessResponse;
import com.hellduo.domain.payment.entity.Payment;
import com.hellduo.domain.payment.exception.PaymentErrorCode;
import com.hellduo.domain.payment.exception.PaymentException;
import com.hellduo.domain.payment.repository.PaymentRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import com.hellduo.domain.user.service.PointService;
import com.hellduo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class PaymentService {
    private final UserService userService;
    private final PointService pointService;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Value("${payment.toss.secret-key}")
    private String secretKey;
    @Value("${payment.toss.client-key}")
    private String clientKey;
    @Value("${payment.toss.success-url}")
    private String successUrl;
    @Value("${payment.toss.fail-url}")
    private String failUrl;

    @Transactional
    public PaymentToApiRequest requestPayment(Long userId, PaymentFromUserRequest req) {
        User user = userRepository.findUserByIdWithThrow(userId);

        Payment payment = Payment.builder().
                user(user).
                amount(req.amount()).
                orderName(req.orderName()).
                orderId(UUID.randomUUID().toString()).
                build();

        paymentRepository.save(payment);

        return new PaymentToApiRequest(
                payment.getAmount(),
                payment.getOrderName(),
                payment.getOrderId(),
                payment.getUser().getEmail(),
                payment.getUser().getName(),
                successUrl,
                failUrl,
                payment.getCreatedAt());
    }

    public PaymentSuccessResponse successPayment(String jsonBody) {
        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;

        try {
            // 클라이언트에서 받은 JSON 요청 바디
            JSONObject requestData = (JSONObject)parser.parse(jsonBody);
            paymentKey = (String)requestData.get("paymentKey");
            orderId = (String)requestData.get("orderId");
            amount = (String)requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Long amountLong = Long.parseLong(amount);
        Payment payment = verifyPayment(orderId, amountLong);
        payment.addPaymentKey(paymentKey);

        User user = userRepository.findUserByIdWithThrow(payment.getUser().getId());
        pointService.chargePoint(user, amountLong, orderId);

        return requestPaymentAccept(paymentKey, orderId, amountLong);

    }

    public String getKey() {
        return this.clientKey;
    }

    private PaymentSuccessResponse requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("amount", amount);

        PaymentSuccessResponse response = null;

        try {
            response = restTemplate.postForObject("https://api.tosspayments.com/v1/payments/" + paymentKey,
                    new HttpEntity<>(params, headers),
                    PaymentSuccessResponse.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder().encode((secretKey + ":").getBytes(StandardCharsets.UTF_8))
        );

        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private Payment verifyPayment(String orderId, Long amount) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new PaymentException(PaymentErrorCode.NOT_FOUND_ORDER_ID)
        );

        if (!payment.getAmount().equals(amount)) {
            throw new PaymentException(PaymentErrorCode.NOT_EQUALS_AMOUNT);
        }

        return payment;
    }

}
