package com.hellduo.domain.review.entity;

import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Double rating;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_id")
    private PT pt;

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
