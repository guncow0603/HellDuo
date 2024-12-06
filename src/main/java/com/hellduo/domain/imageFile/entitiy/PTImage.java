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
@Table(name = "tb_pt_image")
public class PTImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("S3 URL")
    @Column(name = "user_image_url", nullable = false)
    private String userImageUrl;

    @Comment("프로필 이미지 or 자격증 이미지")
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ImageType type;

    @Comment("유저 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Builder
    private PTImage(String userImageUrl, User user, ImageType type) {
        this.userImageUrl = userImageUrl;
        this.user = user;
        this.type = type;
    }
}