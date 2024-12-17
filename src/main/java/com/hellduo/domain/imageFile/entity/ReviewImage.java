package com.hellduo.domain.imageFile.entity;

import com.hellduo.domain.review.dto.entity.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
@Getter
@NoArgsConstructor
@Entity(name = "tb_review_image")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("S3 URL")
    @Column(name = "review_image_url", nullable = false)
    private String reviewImageUrl;

    @Comment("리뷰 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    private ReviewImage(String reviewImageUrl,Review review) {
        this.reviewImageUrl = reviewImageUrl;
        this.review=review;
    }
}
