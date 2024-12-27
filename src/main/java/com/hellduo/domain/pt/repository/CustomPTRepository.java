package com.hellduo.domain.pt.repository;

import com.hellduo.domain.pt.dto.response.PTsReadRes;
import com.hellduo.domain.pt.entity.enums.PTSpecialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CustomPTRepository {
    Page<PTsReadRes> searchPTs(Pageable pageable, String keyword, PTSpecialization category);
}
