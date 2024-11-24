package com.hellduo.domain.pt.repository;

import com.hellduo.domain.pt.entity.PT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PTRepository extends JpaRepository<PT, Long> {
}
