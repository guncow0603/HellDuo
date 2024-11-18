package com.hellduo.domain.user.repository;

import com.hellduo.domain.user.entity.Trainer;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    Optional<Trainer> findByEmail(String email);
    Optional<Trainer> findByName(String name);
    Optional<Trainer> findByPhoneNumber(String phoneNumber);

    @Cacheable(value = "trainer", key = "#email")
    default Trainer findTrainerByEmailWithThrow(String email) {
        return findByEmail(email).orElseThrow(() ->
                new UserException(UserErrorCode.NOT_FOUND_USER));
    }

    @Cacheable(value = "trainer", key = "#trainerId")
    default Trainer findTrainerByIdWithThrow(Long trainerId) {
        return findById(trainerId).orElseThrow(() ->
                new UserException(UserErrorCode.NOT_FOUND_USER));
    }
}
