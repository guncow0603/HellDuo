package com.hellduo.domain.user.entity;

import com.hellduo.domain.common.BaseEntity;
import com.hellduo.domain.user.entity.enums.PointType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "tb_point_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("변동된 포인트")
    @Column(name = "change_point", nullable = false)
    private Long changePoint;

    @Comment("변동 타입")
    @Enumerated(EnumType.STRING)
    private PointType type;

    @Builder
    public Point(User user, Long changePoint, PointType type) {
        this.user = user;
        this.changePoint = changePoint;
        this.type = type;
    }
}