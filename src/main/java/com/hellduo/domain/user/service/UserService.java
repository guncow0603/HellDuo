package com.hellduo.domain.user.service;

import com.hellduo.domain.user.dto.request.UserLoginReq;
import com.hellduo.domain.user.dto.request.UserSignupReq;
import com.hellduo.domain.user.dto.response.UserLoginRes;
import com.hellduo.domain.user.dto.response.UserOwnProfileGetRes;
import com.hellduo.domain.user.dto.response.UserSignupRes;
import com.hellduo.domain.user.entity.Trainer;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.TrainerRepository;
import com.hellduo.domain.user.repository.UserRepository;
import com.hellduo.global.jwt.JwtUtil;
import com.hellduo.global.redis.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final TrainerRepository trainerRepository;

    @Value("${admin-token}")
    private String ADMIN_TOKEN;

    public UserSignupRes signup(UserSignupReq req) {
        String email = req.email();
        String password = passwordEncoder.encode(req.password());
        String passwordConfirm = req.passwordConfirm();
        String adminToken = req.adminToken();
        String gender = req.gender();
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

        userRepository.save(user);

        return new UserSignupRes("회원 가입 완료");
    }

    public UserLoginRes login(UserLoginReq req, HttpServletResponse res) {
        String email = req.email();
        String password = req.password();
        User user1;
        User user;
        Trainer trainer1;
        Trainer trainer;

        if(req.isUserTypeTrainer().equals(true)) {
            user1 = userRepository.findUserByEmailWithThrow(email);
            user = userRepository.findUserByIdWithThrow(user1.getId());

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new UserException(UserErrorCode.BAD_LOGIN);
            }

            String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
            String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

            jwtUtil.addAccessJwtToCookie(accessToken, res);
            jwtUtil.addRefreshJwtToCookie(refreshToken, res);
            refreshTokenService.saveRefreshToken(refreshToken, user.getId());
        }
        else
        {
            trainer1 = trainerRepository.findTrainerByEmailWithThrow(email);
            trainer = trainerRepository.findTrainerByIdWithThrow(trainer1.getId());

            if (!passwordEncoder.matches(password, trainer.getPassword())) {
                throw new UserException(UserErrorCode.BAD_LOGIN);
            }

            String accessToken = jwtUtil.createAccessToken(trainer.getEmail(), trainer.getRole());
            String refreshToken = jwtUtil.createRefreshToken(trainer.getEmail());

            jwtUtil.addAccessJwtToCookie(accessToken, res);
            jwtUtil.addRefreshJwtToCookie(refreshToken, res);
            refreshTokenService.saveRefreshToken(refreshToken, trainer.getId());
        }


        return new UserLoginRes("로그인 완료");
    }

    public UserOwnProfileGetRes getOwnProfile(Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);

        String email = user.getEmail();          // 이메일
        String gender = user.getGender();        // 성별
        Integer age = user.getAge();             // 나이
        String phoneNumber = user.getPhoneNumber(); // 전화번호
        String nickname = user.getNickname();    // 닉네임
        Double weight = user.getWeight();        // 체중
        Double height = user.getHeight();        // 키
        return new UserOwnProfileGetRes(user.getId(),
                email,
                gender,
                age,
                phoneNumber,
                nickname,
                weight,
                height);
    }
}
