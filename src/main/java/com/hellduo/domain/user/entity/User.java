package com.hellduo.domain.user.entity;

import com.hellduo.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_USER")
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원 이메일")
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;  // 이메일

    @Comment("비밀번호")
    @Column(name = "password", nullable = false)
    private String password;  // 비밀번호

    @Comment("성별")
    @Column(name = "gender", length = 10, nullable = false)
    private String gender;  // 성별

    @Comment("연령")
    @Column(name = "age", nullable = false)
    private Integer age;  // 나이

    @Comment("전화번호")
    @Column(name = "phone_number", length = 20, nullable = false, unique = true)
    private String phoneNumber;  // 전화번호

    @Comment("닉네임")
    @Column(name = "nickname", length = 50, nullable = false, unique = true)
    private String nickname;  // 닉네임

    @Comment("체중")
    @Column(name = "weight", nullable = false)
    private Double weight;  // 체중

    @Comment("키")
    @Column(name = "height", nullable = false)
    private Double height;  // 키

    @Comment("회원 역할")
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRoleType role;  // 역할

    @Builder
    public User(String email, String password, UserRoleType role, String nickname,String gender,
                Integer age, Double weight,Double height, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

}
