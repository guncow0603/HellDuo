package com.hellduo.domain.pt.repository;

import com.hellduo.domain.pt.dto.response.PTsReadRes;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.QPT;
import com.hellduo.domain.pt.entity.enums.PTSpecialization;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

import static com.hellduo.domain.pt.entity.QPT.pT;

@RequiredArgsConstructor
public class CustomPTRepositoryImpl implements CustomPTRepository {

    private final JPAQueryFactory jpaQueryFactory;
    // QPT 객체 생성
    QPT qPT = pT;

    @Override
    public Page<PTsReadRes> searchPTs(Pageable pageable, String keyword, PTSpecialization category) {

        // BooleanBuilder로 동적 쿼리 조건 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 조건 추가
        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.and(qPT.title.containsIgnoreCase(keyword)
                    .or(qPT.description.containsIgnoreCase(keyword)));
        }

        // 카테고리 조건 추가
        if (category != null) {
            builder.and(qPT.specialization.eq(category));
        }

        // 쿼리 생성
        JPAQuery<PT> query = jpaQueryFactory
                .selectFrom(qPT)
                .where(builder); // 동적 쿼리 조건

        // 페이징 처리
        query.offset(pageable.getPageNumber() * pageable.getPageSize()) // offset 설정
                .limit(pageable.getPageSize()); // limit 설정

        // 정렬 설정
        query.orderBy(getSortOrder(pageable.getSort())); // 정렬 적용

        // 결과 조회
        List<PT> result = query.fetch();

        // 총 개수 구하기
        long total = query.fetchCount();

        // PTsReadRes로 변환
        List<PTsReadRes> resultList = result.stream()
                .map(pt -> new PTsReadRes(
                        pt.getId(), // PT ID
                        pt.getTitle(), // PT 제목
                        pt.getSpecialization() != null ? pt.getSpecialization().getName() : null, // specialization 이름 (Enum -> String)
                        pt.getScheduledDate(), // scheduledDate
                        pt.getPrice(), // price
                        pt.getStatus().getDescription() // ptStatus
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, pageable, total);
    }

    // 정렬 조건 생성
    private com.querydsl.core.types.OrderSpecifier<?> getSortOrder(Sort sort) {
        QPT qPT = pT;

        // 정렬 기준과 방향을 처리
        Sort.Order order = sort.isEmpty() ? new Sort.Order(Sort.Direction.ASC, "createdAt") : sort.iterator().next();  // 기본값은 createdAt 기준

        if ("title".equals(order.getProperty())) {
            return order.isAscending() ? qPT.title.asc() : qPT.title.desc();
        } else if ("createdAt".equals(order.getProperty())) {
            return order.isAscending() ? qPT.createdAt.asc() : qPT.createdAt.desc();
        } else {
            return order.isAscending() ? qPT.id.asc() : qPT.id.desc(); // 기본 정렬 기준
        }
    }
}