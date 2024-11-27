package com.hellduo.domain.pt.repository;

import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PTRepository extends JpaRepository<PT, Long> {
    default PT findPTByIdWithThrow(Long id) {
        return findById(id).orElseThrow(()->
                new PTException(PTErrorCode.PT_NOT_FOUND));
    };
}
