package com.hellduo.domain.payment.entity;

import com.hellduo.domain.common.BaseEntity;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "PT_Payments")
@RequiredArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("결제 키")
    @Column(name = "payment_key")
    private String paymentKey;

    @Comment("주문 번호")
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Comment("결제 금액")
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Comment("주문 이름")
    @Column(name = "order_name", nullable = false)
    private String orderName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Payment(String paymentKey, String orderId, Long amount, String orderName, User user) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.orderName = orderName;
        this.user = user;
    }

    public void addPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }
}