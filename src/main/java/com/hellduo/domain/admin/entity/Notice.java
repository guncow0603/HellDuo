package com.hellduo.domain.admin.entity;

import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name="tb_notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User user;

    @Builder
    private Notice(String title, String content, User user)
    {
        this.title = title;
        this.content = content;
        this.user = user;

    }
    public void updateTitle(String title)
    {
        this.title = title;
    }
    public void updateContent(String content)
    {
        this.content = content;
    }
}
