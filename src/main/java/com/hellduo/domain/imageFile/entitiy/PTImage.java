package com.hellduo.domain.imageFile.entitiy;

import com.hellduo.domain.imageFile.entitiy.enums.ImageType;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_pt_image")
public class PTImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("S3 URL")
    @Column(name = "user_image_url", nullable = false)
    private String userImageUrl;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ImageType type;

    @Comment("피티 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_id")
    private PT pt;

    @Comment("유저 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private PTImage(String userImageUrl, PT pt,User user, ImageType type) {
        this.userImageUrl = userImageUrl;
        this.pt = pt;
        this.user=user;
        this.type = type;
    }
}