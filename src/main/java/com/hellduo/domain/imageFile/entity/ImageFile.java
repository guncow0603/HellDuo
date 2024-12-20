package com.hellduo.domain.imageFile.entity;

import com.hellduo.domain.imageFile.entity.enums.ImageType;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@Entity(name = "TB_IMAGE_FILE")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("S3 URL")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Comment("프로필 이미지 or 자격증 이미지 or 피티 이미지 or 리뷰 이미지 or 게시판 이미지")
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ImageType type;

    @Comment("연관된 엔티티의 ID (User, PT, Review 등)")
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private ImageFile(String imageUrl, ImageType type,Long targetId,User user) {
        this.imageUrl = imageUrl;
        this.type = type;
        this.targetId = targetId;
        this.user=user;
    }
}
