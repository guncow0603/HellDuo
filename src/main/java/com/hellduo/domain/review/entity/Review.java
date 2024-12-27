package com.hellduo.domain.review.entity;

import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity(name ="tb_review")
@RequiredArgsConstructor
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title; // 제목 (최대 100자)

    @Column(nullable = false, length = 1000)
    private String content; // 내용 (최대 1000자)

    @Column(nullable = false)
    @Min(0)
    @Max(5)
    private Double rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_id", nullable = false)
    private PT pt; // 해당 리뷰가 속한 PT 세션 (일대일 관계)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @Builder
    public Review(String title, String content, PT pt, User trainer, Double rating) {
        this.title = title;
        this.content = content;
        this.pt = pt;
        this.trainer = trainer;
        this.rating = rating;
    }
}
