package com.hellduo.domain.board.repository;

import com.hellduo.domain.board.dto.response.BoardsReadRes;
import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.entity.QBoard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QBoard board = QBoard.board;

    @Override
    public Page<BoardsReadRes> searchBoards(Pageable pageable, String keyword) {

        // 쿼리 생성
        JPAQuery<Board> query = jpaQueryFactory
                .selectFrom(board)
                .where(board.title.contains(keyword));


        // 정렬 적용
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                PathBuilder<Board> pathBuilder = new PathBuilder<>(Board.class, board.getMetadata());
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                        pathBuilder.get(order.getProperty(), Comparable.class)));
            }
        }

        List<BoardsReadRes> results = query
                .offset(pageable.getOffset())  // Pageable로부터 오프셋 정보 추출
                .limit(pageable.getPageSize()) // Pageable로부터 사이즈 정보 추출
                .select(Projections.constructor(
                        BoardsReadRes.class,
                        board.id,
                        board.title,
                        board.likeCount
                ))
                .fetch();

        // 전체 카운트 쿼리 실행 (fetchCount()로 전체 카운트)
        return PageableExecutionUtils.getPage(results, pageable,
                query::fetchCount); // 전체 개수를 가져오는 쿼리 실행
    }
}