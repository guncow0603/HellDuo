package com.hellduo.domain.user.service;

import com.hellduo.domain.imageFile.entitiy.enums.ImageType;
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

import java.util.ArrayList;
import java.util.List;

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
                .nickname(name)
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

    public UserOwnProfileGetRes getOwnProfile(User user) {
        return new UserOwnProfileGetRes(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getGender().getDescription(),
                user.getAge(),
                user.getPhoneNumber(),
                user.getNickname(),
                user.getWeight(),
                user.getHeight());
    }

    public TrainerOwnProfileGetRes getOwnTrainerProfile(User trainer) {

        return new TrainerOwnProfileGetRes(trainer.getId(),
                trainer.getEmail(),
                trainer.getName(),
                trainer.getPhoneNumber(),
                trainer.getGender().getDescription(),
                trainer.getAge(),
                trainer.getSpecialization().getName(),
                trainer.getExperience(),
                trainer.getCertifications(),
                trainer.getBio(),
                trainer.getRating());
    }

    public UserProfileUpdateRes updateUserProfile(User user, UserProfileUpdateReq req) {
        if (userRepository.findByPhoneNumber(req.phoneNumber()).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }
        if (userRepository.findByNickname(req.nickname()).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_NICKNAME);
        }
        // 전화번호 업데이트
        if (req.phoneNumber() != null && !req.phoneNumber().isEmpty()) { user.updatePhoneNumber(req.phoneNumber()); }
        // 나이 업데이트
        if (req.age() != null) { user.updateAge(req.age()); }
        // 닉네임 업데이트
        if (req.nickname() != null && !req.nickname().isEmpty()) { user.updateNickName(req.nickname()); }
        // 체중 업데이트
        if (req.weight() != null) { user.updateWeight(req.weight()); }
        // 키 업데이트
        if (req.height() != null) { user.updateHeight(req.height()); }

        return new UserProfileUpdateRes("수정 완료");
    }

    public TrainerProfileUpdateRes updateTrainerProfile(User trainer, TrainerProfileUpdateReq req) {


        if (userRepository.findByPhoneNumber(req.phoneNumber()).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }

        // 전화번호 업데이트
        if (req.phoneNumber() != null && !req.phoneNumber().isEmpty()) { trainer.updatePhoneNumber(req.phoneNumber()); }
        // 전문 분야 업데이트
        if (req.specialization() != null) { trainer.updateSpecialization(req.specialization()); }
        // 경력 연수 업데이트
        if (req.experience() != null) { trainer.updateExperience(req.experience()); }
        // 자격증 정보 업데이트
        if (req.certifications() != null && !req.certifications().isEmpty()) {
            trainer.updateCertifications(req.certifications());
        }
        // 자기소개 업데이트
        if (req.bio() != null && !req.bio().isEmpty()) { trainer.updateBio(req.bio()); }

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

    public TrainerOwnProfileGetRes getTrainerProfile(Long trainerId) {
        User trainer = userRepository.findUserByIdWithThrow(trainerId);
        return new TrainerOwnProfileGetRes(trainer.getId(),
                trainer.getEmail(),
                trainer.getName(),
                trainer.getPhoneNumber(),
                trainer.getGender().getDescription(),
                trainer.getAge(),
                trainer.getSpecialization().getName(),
                trainer.getExperience(),
                trainer.getCertifications(),
                trainer.getBio(),
                trainer.getRating());
    }

    public List<BestRatingTrainerRes> getBestRatingTrainer() {
        // 전체 유저 중에서 트레이너 역할(UserRoleType이 TRAINER)인 사용자만 필터링
        List<User> allUsers = userRepository.findAll();

        List<User> trainers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getRole() == UserRoleType.TRAINER && !user.isDeleted()) { // 트레이너이면서 탈퇴하지 않은 유저
                trainers.add(user);
            }
        }

        // 평점 순으로 정렬
        for (int i = 0; i < trainers.size(); i++) {
            for (int j = i + 1; j < trainers.size(); j++) {
                if (trainers.get(i).getRating() < trainers.get(j).getRating()) {
                    // swap
                    User temp = trainers.get(i);
                    trainers.set(i, trainers.get(j));
                    trainers.set(j, temp);
                }
            }
        }

        // 상위 10명만 추출
        List<User> top10Trainers = new ArrayList<>();
        for (int i = 0; i < trainers.size() && i < 10; i++) {
            top10Trainers.add(trainers.get(i));
        }

        // DTO 변환
        List<BestRatingTrainerRes> result = new ArrayList<>();
        for (User trainer : top10Trainers) {
            result.add(new BestRatingTrainerRes(trainer.getId(),
                    trainer.getName(),
                    trainer.getRating(),
                    trainer.getSpecialization().getName()));
        }

        return result;
    }
}
