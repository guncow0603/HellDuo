package com.hellduo.domain.user.service;

import com.hellduo.domain.imageFile.entitiy.ImageType;
import com.hellduo.domain.imageFile.entitiy.UserImage;
import com.hellduo.domain.imageFile.repository.UserImageRepository;
import com.hellduo.domain.user.dto.request.*;
import com.hellduo.domain.user.dto.response.*;
import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.Specialization;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.UserRepository;
import com.hellduo.global.jwt.JwtUtil;
import com.hellduo.global.redis.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    @Value("${admin-token}")
    private String ADMIN_TOKEN;

    public UserSignupRes signup(UserSignupReq req) {
        String email = req.email();
        String password = passwordEncoder.encode(req.password());
        String passwordConfirm = req.passwordConfirm();
        String name = req.name();;
        String adminToken = req.adminToken();
        Gender gender = req.gender();
        Integer age = req.age();
        String phoneNumber = req.phoneNumber();
        String nickname = req.nickname();
        Double weight = req.weight();
        Double height = req.height();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_NICKNAME);
        }

        UserRoleType role = UserRoleType.USER;
        if (req.admin()) {
            if (!ADMIN_TOKEN.equals(adminToken)) {
                throw new UserException(UserErrorCode.INVALID_ADMIN_CODE);
            }
            role = UserRoleType.ADMIN;
        }
        if (!passwordEncoder.matches(passwordConfirm, password)) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(role)
                .nickname(nickname)
                .gender(gender)
                .age(age)
                .phoneNumber(phoneNumber)
                .weight(weight)
                .height(height)
                .build();
        UserImage userImage = UserImage.builder()
                .userImageUrl("https://i.ibb.co/7gD22Tg/2024-11-22-10-01-08.png")
                .type(ImageType.PROFILE_IMG)
                .user(user)
                .build();


        userRepository.save(user);
        userImageRepository.save(userImage);
        return new UserSignupRes("회원 가입 완료");
    }

    public TrainerSignupRes trainerSignup(TrainerSignupReq req) {
        String email = req.email();
        String password = passwordEncoder.encode(req.password());
        String passwordConfirm = req.passwordConfirm();
        String name = req.name();
        String phoneNumber = req.phoneNumber();
        Gender gender = req.gender();
        Integer age = req.age();
        Specialization specialization = req.specialization();
        Integer experience = req.experience();
        String certifications = req.certifications();
        String bio = req.bio();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }

        UserRoleType role = UserRoleType.TRAINER;

        if (!passwordEncoder.matches(passwordConfirm, password)) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }

        User trainer = User.builder()
                .email(email)
                .password(password)
                .role(role)
                .name(name)
                .gender(gender)
                .age(age)
                .specialization(specialization)
                .phoneNumber(phoneNumber)
                .experience(experience)
                .certifications(certifications)
                .bio(bio)
                .build();

        UserImage userImage = UserImage.builder()
                .userImageUrl("https://i.ibb.co/7gD22Tg/2024-11-22-10-01-08.png")
                .type(ImageType.PROFILE_IMG)
                .user(trainer)
                .build();

        userRepository.save(trainer);
        userImageRepository.save(userImage);

        return new TrainerSignupRes("회원 가입 완료");
    }

    public UserLoginRes login(UserLoginReq req, HttpServletResponse res) {
        String email = req.email();
        String password = req.password();
        User user1;
        User user;

            user1 = userRepository.findUserByEmailWithThrow(email);
            user = userRepository.findUserByIdWithThrow(user1.getId());

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new UserException(UserErrorCode.BAD_LOGIN);
            }
            if(user.isDeleted()){
                throw new UserException(UserErrorCode.DELETED_USER);
            }

            String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
            String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

            jwtUtil.addAccessJwtToCookie(accessToken, res);
            jwtUtil.addRefreshJwtToCookie(refreshToken, res);
            refreshTokenService.saveRefreshToken(refreshToken, user.getId());

        return new UserLoginRes("로그인 완료");
    }

    public UserOwnProfileGetRes getOwnProfile(Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);

        String name = user.getName();            //이름
        String email = user.getEmail();          // 이메일
        Gender gender = user.getGender();        // 성별
        Integer age = user.getAge();             // 나이
        String phoneNumber = user.getPhoneNumber(); // 전화번호
        String nickname = user.getNickname();    // 닉네임
        Double weight = user.getWeight();        // 체중
        Double height = user.getHeight();        // 키
        return new UserOwnProfileGetRes(user.getId(),
                name,
                email,
                gender.getDescription(),
                age,
                phoneNumber,
                nickname,
                weight,
                height);
    }

    public TrainerOwnProfileGetRes getOwnTrainerProfile(Long userId) {
        User trainer = userRepository.findUserByIdWithThrow(userId);

        String email = trainer.getEmail();          // 이메일
        String name = trainer.getName();        // 성별          // 나이
        String phoneNumber = trainer.getPhoneNumber(); // 전화번호
        Gender gender = trainer.getGender();    // 닉네임
        Specialization specialization = trainer.getSpecialization();        // 체중
        Integer experience = trainer.getExperience();        // 키
        String certifications = trainer.getCertifications();
        String bio = trainer.getBio();
        Integer age = trainer.getAge(); // 나이
        return new TrainerOwnProfileGetRes(trainer.getId(),
                email,
                name,
                phoneNumber,
                gender.getDescription(),
                age,
                specialization,
                experience,
                certifications,
                bio);
    }

    public UserProfileUpdateRes updateUserProfile(Long userId, UserProfileUpdateReq req) {
        User user = userRepository.findUserByIdWithThrow(userId);
        String phoneNumber = req.phoneNumber(); // 전화번호
        Integer age = req.age();             // 나이
        String nickname = req.nickname();    // 닉네임
        Double weight = req.weight();        // 체중
        Double height = req.height();        // 키

        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_NICKNAME);
        }
        // 전화번호 업데이트
        if (phoneNumber != null && !phoneNumber.isEmpty()) { user.updatePhoneNumber(req.phoneNumber()); }
        // 나이 업데이트
        if (age != null) { user.updateAge(age); }
        // 닉네임 업데이트
        if (nickname != null && !nickname.isEmpty()) { user.updateNickName(nickname); }
        // 체중 업데이트
        if (weight != null) { user.updateWeight(weight); }
        // 키 업데이트
        if (height != null) { user.updateHeight(height); }

        return new UserProfileUpdateRes("수정 완료");
    }

    public TrainerProfileUpdateRes updateTrainerProfile(Long userId, TrainerProfileUpdateReq req) {
        User trainer = userRepository.findUserByIdWithThrow(userId);

        String phoneNumber = req.phoneNumber(); // 전화번호
        Specialization specialization = req.specialization(); // 전문 분야
        Integer experience = req.experience();  // 경력 연수
        String certifications = req.certifications(); // 자격증 정보
        String bio = req.bio();                 // 자기소개

        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }

        // 전화번호 업데이트
        if (phoneNumber != null && !phoneNumber.isEmpty()) { trainer.updatePhoneNumber(phoneNumber); }
        // 전문 분야 업데이트
        if (specialization != null) { trainer.updateSpecialization(specialization); }
        // 경력 연수 업데이트
        if (experience != null) { trainer.updateExperience(experience); }
        // 자격증 정보 업데이트
        if (certifications != null && !certifications.isEmpty()) {
            trainer.updateCertifications(certifications);
        }
        // 자기소개 업데이트
        if (bio != null && !bio.isEmpty()) { trainer.updateBio(bio); }

        return new TrainerProfileUpdateRes("수정 완료");
    }

    public UserWithdrawalRes withdrawal(UserWithdrawalReq req, Long userId, HttpServletResponse response) {
        String password = req.password();

        // 사용자 정보 조회
        User user = userRepository.findUserByIdWithThrow(userId);

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.BAD_LOGIN);
        }

        // 탈퇴 처리
        user.withdrawal();

        // 로그아웃 직접 처리
        triggerLogout(response);

        return new UserWithdrawalRes("탈퇴 완료");
    }

    private void triggerLogout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        Cookie accessTokenCookie = new Cookie("AccessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("RefreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    public UserLogoutRes logout(HttpServletResponse response) {
        triggerLogout(response);
        return new UserLogoutRes("로그아웃 완료");
    }
}
