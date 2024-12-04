package com.hellduo.domain.user.entity;

import com.hellduo.domain.common.BaseEntity;
import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.Specialization;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_USER")
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("이메일")
    @Column(name = "email", length = 50, unique = true)
    private String email;  // 이메일

    @Comment("비밀번호")
    @Column(name = "password")
    private String password;  // 비밀번호

    @Comment("성별")
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Comment("연령")
    @Column(name = "age")
    private Integer age;  // 나이

    @Comment("전화번호")
    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;  // 전화번호

    @Comment("닉네임")
    @Column(name = "nickname", length = 50, unique = true)
    private String nickname;  // 닉네임

    @Comment("체중")
    @Column(name = "weight")
    private Double weight;  // 체중

    @Comment("키")
    @Column(name = "height")
    private Double height;  // 키

    @Comment("회원 역할")
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRoleType role;  // 역할

    @Comment("이름")
    @Column(name = "name")
    private String name;  // 이름

    @Comment("전문 분야")
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;  // 전문 분야

    @Comment("경력")
    @Column(name = "experience")
    private Integer experience;  // 경력(년수)

    @Comment("자격증")
    @Column(name = "certifications")
    private String certifications;  // 자격증

    @Comment("자기소개")
    @Column(name = "bio")
    private String bio;  // 자기소개

    @Comment("탈퇴 여부")
    @Column(name = "deleted")
    private boolean deleted = false;

    @Comment("회원 보유 포인트")
    @Column(name = "point", nullable = false)
    @ColumnDefault("0")
    private Long point = 0L;


    @Builder
    public User(String email, String password, UserRoleType role, String nickname, Gender gender,
                Integer age, Double weight, Double height, String phoneNumber,
                String name, Specialization specialization, Integer experience,
                String certifications, String bio) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.certifications = certifications;
        this.bio = bio;
    }

    public void updateNickName(String nickname) { this.nickname = nickname; }
    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void updateAge(Integer age) {
        this.age = age;
    }
    public void updateWeight(Double weight) {
        this.weight = weight;
    }
    public void updateHeight(Double height) {
        this.height = height;
    }
    public void updateSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
    public void updateExperience(Integer experience) {
        this.experience = experience;
    }
    public void updateCertifications(String certifications) {
        this.certifications = certifications;
    }
    public void updateBio(String bio) {
        this.bio = bio;
    }
    public void withdrawal() { this.deleted = true; }

    public void addPoint(Long point) {
        this.point += point;
    }
}
