package com.hellduo.domain.user.service;

import com.hellduo.domain.imageFile.entity.ImageFile;
import com.hellduo.domain.imageFile.entity.enums.ImageType;
import com.hellduo.domain.imageFile.repository.ImageFileRepository;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.enums.PTStatus;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.user.dto.request.*;
import com.hellduo.domain.user.dto.response.*;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.Specialization;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.entity.enums.UserStatus;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PTRepository ptRepository;
    private final ImageFileRepository imageFileRepository;
    @Value("${admin_token}")
    private String ADMIN_TOKEN;

    @Transactional
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
                .userStatus(UserStatus.ACTION)
                .build();

        userRepository.save(user);

        ImageFile userImage = ImageFile.builder()
                .imageUrl("https://i.ibb.co/7gD22Tg/2024-11-22-10-01-08.png")
                .type(ImageType.PROFILE_IMG)
                .user(user)
                .targetId(user.getId())
                .build();

        imageFileRepository.save(userImage);

        return new UserSignupRes("회원 가입 완료");
    }
    @Transactional
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
                .userStatus(UserStatus.ACTION)
                .build();

        userRepository.save(trainer);

        ImageFile userImage = ImageFile.builder()
                .imageUrl("https://i.ibb.co/7gD22Tg/2024-11-22-10-01-08.png")
                .type(ImageType.PROFILE_IMG)
                .user(trainer)
                .targetId(trainer.getId())
                .build();

        imageFileRepository.save(userImage);

        return new TrainerSignupRes("회원 가입 완료");
    }

    @Transactional
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
            if(user.getUserStatus().equals(UserStatus.DELETED)){
                throw new UserException(UserErrorCode.DELETED_USER);
            }

            String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
            String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

            jwtUtil.addAccessJwtToCookie(accessToken, res);
            jwtUtil.addRefreshJwtToCookie(refreshToken, res);
            refreshTokenService.saveRefreshToken(refreshToken, user.getId());

        return new UserLoginRes("로그인 완료");
    }

    @Transactional(readOnly = true)
    public UserOwnProfileGetRes getOwnProfile(User user) {
        return new UserOwnProfileGetRes(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getGender().getDescription(),
                user.getAge(),
                user.getPhoneNumber(),
                user.getNickname(),
                user.getWeight(),
                user.getHeight());
    }

    @Transactional(readOnly = true)
    public TrainerOwnProfileGetRes getOwnTrainerProfile(User trainer) {

        return new TrainerOwnProfileGetRes(
                trainer.getId(),
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

    @Transactional
    public UserProfileUpdateRes updateUserProfile(Long userId, UserProfileUpdateReq req) {
        User user= userRepository.findUserByIdWithThrow(userId);

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

    @Transactional
    public TrainerProfileUpdateRes updateTrainerProfile(Long trainerId, TrainerProfileUpdateReq req) {
        User trainer = userRepository.findUserByIdWithThrow(trainerId);

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

    @Transactional
    public UserWithdrawalRes withdrawal(UserWithdrawalReq req, Long userId, HttpServletResponse response) {
        String password = req.password();

        // 사용자 정보 조회
        User user = userRepository.findUserByIdWithThrow(userId);

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.BAD_LOGIN);
        }

        // 탈퇴 처리
        user.updateUserStatus(UserStatus.DELETED);

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

    @Transactional
    public UserLogoutRes logout(HttpServletResponse response) {
        triggerLogout(response);
        return new UserLogoutRes("로그아웃 완료");
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Cacheable(value = "trainerBestRatingCache", key = "'bestRatingTrainer'", unless = "#result == null or #result.size() == 0", cacheManager = "redisCacheManager")
    public List<BestRatingTrainerRes> getBestRatingTrainer() {
        List<User> top10Trainers = userRepository.findTop10ByRoleAndUserStatusNotOrderByRatingDesc(UserRoleType.TRAINER, UserStatus.DELETED);

        // DTO 변환
        return top10Trainers.stream()
                .map(trainer -> new BestRatingTrainerRes(trainer.getId(),
                        trainer.getName(),
                        trainer.getRating(),
                        trainer.getSpecialization().getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserOwnProfileGetRes getUserProfile(User trainer, Long ptId) {
        PT pt = ptRepository.findPTByIdWithThrow(ptId);
        if(!pt.getTrainer().getId().equals(trainer.getId())){
            throw new PTException(PTErrorCode.TRAINER_ID_MISMATCH);
        }
        if(pt.getStatus().equals(PTStatus.UNRESERVED)){
            throw new PTException(PTErrorCode.PT_UNRESERVED);
        }

        User user = pt.getUser();
        return new UserOwnProfileGetRes(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getGender().getDescription(),
                user.getAge(),
                user.getPhoneNumber(),
                user.getNickname(),
                user.getWeight(),
                user.getHeight()
        );
    }
}
