package com.hellduo.domain.user.repository;

import com.hellduo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String email);
    Optional<User> findByPhoneNumber(String email);
}
