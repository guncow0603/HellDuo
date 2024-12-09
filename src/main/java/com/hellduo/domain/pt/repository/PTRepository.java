package com.hellduo.domain.pt.repository;

import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PTRepository extends JpaRepository<PT, Long> {
    default PT findPTByIdWithThrow(Long id) {
        return findById(id).orElseThrow(()->
                new PTException(PTErrorCode.PT_NOT_FOUND));
    };

    @Query("SELECT p FROM PT p WHERE " +
            "(:keyword IS NULL OR p.title LIKE %:keyword%) AND " +
            "(:category IS NULL OR p.specialization = :category)")
    List<PT> searchByKeywordAndCategory(String keyword, String category, Sort sort);
}
