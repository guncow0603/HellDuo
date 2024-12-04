package com.hellduo.domain.admin.entity;

import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name="TB_Notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
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
