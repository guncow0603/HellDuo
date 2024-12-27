package com.hellduo.domain.admin.entity;

import com.hellduo.domain.admin.entity.enums.ReportReason;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity(name = "TB_REPORT")
@RequiredArgsConstructor
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //신고 받은 사람 아이디
    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    //신고 한 사람
    @ManyToOne
    @JoinColumn(name = "reporter_user_id")
    private User reporterUser;

    @Column(name= "report_reason")
    @Enumerated(EnumType.STRING)
    private ReportReason reportReason;

    @Column(name = "content")
    private String content;



    @Builder
    public Report(String content, ReportReason reportReason, User reportedUser, User reporterUser) {
        this.reporterUser = reporterUser;
        this.reportedUser = reportedUser;
        this.content = content;
        this.reportReason = reportReason;
    }

}
