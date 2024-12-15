package com.hellduo.domain.imageFile.entity;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.review.entity.Review;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_review_image")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("S3 URL")
    @Column(name = "review_image_url", nullable = false)
    private String reviewImageUrl;

    @Comment("유저 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("보드 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;


    @Builder
    private ReviewImage(String reviewImageUrl, User user,Review review) {
        this.reviewImageUrl = reviewImageUrl;
        this.user = user;
        this.review=review;
    }
}
