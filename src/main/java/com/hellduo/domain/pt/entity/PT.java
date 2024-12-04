package com.hellduo.domain.pt.entity;

import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "TB_PT")
public class PT {

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
    private Integer price; // PT 세션 비용

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

    @Builder
    public PT(String title, LocalDateTime scheduledDate, int price, String description, PTStatus status, User trainer, PTSpecialization specialization){
        this.scheduledDate = scheduledDate;
        this.price = price;
        this.description = description;
        this.status = status;
        this.trainer = trainer;
        this.title = title;
        this.specialization = specialization;
    }

    public void updateTitle(String title){ this.title=title; }
    public void updateDescription(String description){ this.description=description; }
    public void updateScheduledDate(LocalDateTime scheduledDate){ this.scheduledDate=scheduledDate; }
    public void updatePrice(Integer price){ this.price=price; }
    public void updateSpecialization(PTSpecialization specialization){ this.specialization=specialization; }
}
