package com.hellduo.domain.user.repository;

import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.entity.enums.UserStatus;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findTop10ByRoleAndUserStatusNotOrderByRatingDesc(UserRoleType role, UserStatus status);

    default User findUserByEmailWithThrow(String email) {
        return findByEmail(email).orElseThrow(() ->
                new UserException(UserErrorCode.NOT_FOUND_USER));
    }
    default User findUserByIdWithThrow(Long userId) {
        return findById(userId).orElseThrow(() ->
                new UserException(UserErrorCode.NOT_FOUND_USER));
    }
}
