package com.hellduo.domain.user.repository;

import com.hellduo.domain.user.entity.Point;
import com.hellduo.domain.user.entity.enums.PointType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point,Long> {
    // 유저 아이디와 충전용 로그만 찾는 쿼리
    List<Point> findByUserIdAndType(Long userId, PointType type);
}
