package com.hellduo.domain.pt.repository;

import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.PTSpecialization;
import com.hellduo.domain.pt.entity.PTStatus;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import io.lettuce.core.dynamic.annotation.Param;
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
            "p.status = :status AND " +
            "(:keyword IS NULL OR p.title LIKE %:keyword%) AND " +
            "(:category IS NULL OR p.specialization = :category)")
    List<PT> searchByKeywordAndCategoryAndStatus(@Param("status") PTStatus status,
                                                 @Param("keyword") String keyword,
                                                 @Param("category") PTSpecialization category,
                                                 Sort sort);

    List<PT> findByStatus(PTStatus status);

    List<PT> findByUserIdAndStatus(Long userId, PTStatus status);

    List<PT> findByTrainerIdAndStatus(Long id, PTStatus ptStatus);

    List<PT> findByUserIdAndStatusAndReviewIsNull(Long userId, PTStatus status);
}
