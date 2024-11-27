package com.hellduo.domain.user.service;

import com.hellduo.domain.common.BaseEntity;
import com.hellduo.domain.user.dto.response.ChargePointRes;
import com.hellduo.domain.user.entity.Point;
import com.hellduo.domain.user.entity.enums.PointType;
import com.hellduo.domain.user.exception.PointErrorCode;
import com.hellduo.domain.user.exception.PointException;
import com.hellduo.domain.user.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService extends BaseEntity {
    private final PointRepository pointRepository;
    public List<ChargePointRes> chargePointRead(Long userId) {
        // 인증된 사용자의 userId와 해당 포인트 로그를 조회하는 userId가 일치하는지 확인
        List<Point> points = pointRepository.findByUserIdAndType(userId, PointType.CHARGE);

        // points가 비어 있으면 예외를 던지거나 적절한 처리를 해주어야 합니다.
        if (points.isEmpty()) {
            throw new PointException(PointErrorCode.NOT_CHARGE_POINT_LOG);
        }

        // ChargePointRes로 변환
        List<ChargePointRes> chargePointResList = new ArrayList<>();
        for (Point point : points) {
            chargePointResList.add(new ChargePointRes(
                    point.getId(),
                    point.getCreatedAt(),
                    point.getChangePoint(),
                    point.getUser().getEmail()
            ));
        }

        return chargePointResList;
    }
}
