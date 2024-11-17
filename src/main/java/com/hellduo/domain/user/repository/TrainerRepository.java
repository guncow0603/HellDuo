package com.hellduo.domain.user.repository;

import com.hellduo.domain.user.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    Optional<Trainer> findByEmail(String email);
    Optional<Trainer> findByName(String name);
    Optional<Trainer> findByPhoneNumber(String phoneNumber);


}
