package com.hellduo.domain.pt.entity;

import com.hellduo.domain.common.BaseEntity;
import com.hellduo.domain.imageFile.entity.PTImage;
import com.hellduo.domain.pt.entity.enums.PTSpecialization;
import com.hellduo.domain.pt.entity.enums.PTStatus;
import com.hellduo.domain.review.dto.entity.Review;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "TB_PT")
public class PT extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PT 세션 ID (Primary Key)

    @Column(nullable = false, length = 100)
    private String title; // PT 제목

    @Column(length = 500)
    private String description; // 설명

    @Column(nullable = false)
    private LocalDateTime scheduledDate; // PT 세션 예약 날짜 및 시간

    @Column(nullable = false)
    private Long price; // PT 세션 비용

    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    @Column(nullable = false)
    private String address; // 주소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer; // 트레이너 정보 (다대일 관계)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 트레이너 정보 (다대일 관계)


    @Enumerated(EnumType.STRING)
    @Column( length = 20)
    private PTStatus status; // PT 상태 (예약/완료/취소 등)

    @Enumerated(EnumType.STRING)
    @Column( length = 20)
    private PTSpecialization specialization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    // PT 이미지와 연결, PT 삭제 시 PTImage도 삭제되도록 Cascade 설정
    @OneToMany(mappedBy = "pt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PTImage> ptImages; // PT와 연결된 이미지들


    @Builder
    public PT(String title, LocalDateTime scheduledDate, Long price, String description,
              PTStatus status, User trainer, PTSpecialization specialization,
              Double latitude,Double longitude,String address){
        this.scheduledDate = scheduledDate;
        this.price = price;
        this.description = description;
        this.status = status;
        this.trainer = trainer;
        this.title = title;
        this.specialization = specialization;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public void updateTitle(String title){ this.title=title; }
    public void updateDescription(String description){ this.description=description; }
    public void updateScheduledDate(LocalDateTime scheduledDate){ this.scheduledDate=scheduledDate; }
    public void updatePrice(Long price){ this.price=price; }
    public void updateSpecialization(PTSpecialization specialization){ this.specialization=specialization; }
    public void updateUser(User user){ this.user=user; }
    public void updateStatus(PTStatus ptStatus){ this.status=ptStatus; }
    public void updateLatitude(Double latitude){ this.latitude=latitude; }
    public void updateLongitude(Double longitude){ this.longitude=longitude; }
    public void updateReviewWritten(Review review) { this.review = review; } // 리뷰 상태 업데이트
}
