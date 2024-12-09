package com.hellduo.domain.imageFile.entitiy;

import com.hellduo.domain.imageFile.entitiy.enums.ImageType;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_banner")
public class BannerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("S3 URL")
    @Column(name = "user_image_url", nullable = false)
    private String userImageUrl;

    @Comment("유저 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Builder
    private BannerImage(String userImageUrl, User user) {
        this.userImageUrl = userImageUrl;
        this.user = user;
    }
}
