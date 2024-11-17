package com.hellduo.domain.user.service;

import com.hellduo.domain.user.dto.request.TrainerSignupReq;
import com.hellduo.domain.user.dto.response.TrainerSignupRes;
import com.hellduo.domain.user.entity.Specialization;
import com.hellduo.domain.user.entity.Trainer;
import com.hellduo.domain.user.entity.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TrainerService {
    private final PasswordEncoder passwordEncoder;
    private final TrainerRepository trainerRepository;
    public TrainerSignupRes signup(TrainerSignupReq req) {
        String email = req.email();
        String password = passwordEncoder.encode(req.password());
        String passwordConfirm = req.passwordConfirm();
        String name = req.name();
        String phoneNumber = req.phoneNumber();
        String gender = req.gender();
        Specialization specialization = req.specialization();
        Integer experience = req.experience();
        String certifications = req.certifications();
        String bio = req.bio();

        if (trainerRepository.findByEmail(email).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }
        if (trainerRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }
        if (trainerRepository.findByName(name).isPresent()) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_NAME);
        }

        UserRoleType role = UserRoleType.TRAINER;

        if (!passwordEncoder.matches(passwordConfirm, password)) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }

        Trainer trainer = Trainer.builder()
                .email(email)
                .password(password)
                .role(role)
                .name(name)
                .gender(gender)
                .specialization(specialization)
                .phoneNumber(phoneNumber)
                .experience(experience)
                .certifications(certifications)
                .bio(bio)
                .build();

        trainerRepository.save(trainer);
        return new TrainerSignupRes("회원 가입 완료");
    }
}
