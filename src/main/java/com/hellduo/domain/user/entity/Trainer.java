package com.hellduo.domain.user.entity;

import com.hellduo.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_TRAINER")
public class Trainer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Comment("트레이너 이메일")
    @Column(name = "email", length = 50, nullable = false, unique = true)
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;  // 이메일

    @Comment("비밀번호")
    @Column(name = "password", nullable = false)
    private String password;  // 비밀번호

    @Comment("성별")
    @Column(name = "gender", length = 10, nullable = false)
    private String gender;  // 성별

    @Comment("이름")
    @Column(name = "name", length = 50, nullable = false)
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;  // 이름

    @Comment("전화번호")
    @Column(name = "phone_number", length = 20, nullable = false, unique = true)
    private String phoneNumber;  // 전화번호

    @Comment("전문 분야")
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private Specialization specialization;  // 전문 분야

    @Comment("경력")
    @Column(name = "experience", nullable = false)
    private Integer experience;  // 경력(년수)

    @Comment("자격증")
    @Column(name = "certifications", nullable = false)
    private String certifications;  // 자격증

    @Comment("자기소개")
    @Column(name = "bio", nullable = false)
    private String bio;  // 자기소개

    @Comment("트레이너 역할")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRoleType role;  // 역할

    @Builder
    public Trainer(String email, String password, String gender, UserRoleType role, String name, String phoneNumber,
                   Specialization specialization, Integer experience, String certifications, String bio){
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.experience = experience;
        this.certifications = certifications;
        this.bio = bio;
    }

}
