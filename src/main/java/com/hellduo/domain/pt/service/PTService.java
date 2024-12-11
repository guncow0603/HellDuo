package com.hellduo.domain.pt.service;

import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.dto.request.PTUpdateReq;
import com.hellduo.domain.pt.dto.response.*;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.PTSpecialization;
import com.hellduo.domain.pt.entity.PTStatus;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.PointErrorCode;
import com.hellduo.domain.user.exception.PointException;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PTService {
    private final UserRepository userRepository;
    private final PTRepository ptRepository;

    public PTCreateRes ptCreate(PTCreateReq req, Long trainerId) {
        User trainer = userRepository.findUserByIdWithThrow(trainerId);

        if(!trainer.getRole().equals(UserRoleType.TRAINER)){
            throw new PTException(PTErrorCode.NOT_TRAiNER);
        }

        PT pt = PT.builder().
                title(req.title()).
                specialization(req.specialization()).
                trainer(trainer).
                scheduledDate(req.scheduledDate()).
                price(req.price()).
                description(req.description()).
                status(PTStatus.UNRESERVED).
                latitude(req.latitude()).
                longitude(req.longitude()).
                address(req.address()).
                build();

        ptRepository.save(pt);

        return new PTCreateRes(pt.getId(),"PT가 생성 되었습니다.");
    }

    public PTReadRes ptRead(Long ptId) {
        PT pt = ptRepository.findPTByIdWithThrow(ptId);
        return new PTReadRes(
                pt.getId(),
                pt.getTrainer().getId(),
                pt.getTitle(),
                pt.getScheduledDate(),
                pt.getPrice(),
                pt.getDescription(),
                pt.getTrainer().getName(),
                pt.getSpecialization().getName(),
                pt.getUser() != null ? pt.getUser().getName() : "미예약",
                pt.getStatus().getDescription(),
                pt.getLatitude(),
                pt.getLongitude());
    }

    public List<getPTsRes> ptsRead(Double userLatitude, Double userLongitude) {
        // UNRESERVED 상태만 가져오기
        List<PT> pts = ptRepository.findByStatus(PTStatus.UNRESERVED);

        // PT 리스트를 거리 기준으로 정렬
        pts.sort(Comparator.comparingDouble(pt -> calculateDistance(userLatitude, userLongitude, pt.getLatitude(), pt.getLongitude())));

        // 응답 객체로 변환
        List<getPTsRes> getPTsResList = new ArrayList<>();
        for (PT pt : pts) {
            getPTsResList.add(new getPTsRes(
                    pt.getId(),
                    pt.getTitle(),
                    pt.getAddress()
            ));
        }
        return getPTsResList;
    }

    // 두 지점 간의 거리를 계산하는 Haversine Formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (단위: km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 (단위: km)
    }

    public PTUpdateRes ptUpdate(Long ptId, PTUpdateReq req, Long trainerId) {
        // 트레이너가 실제 트레이너인지 확인
        User trainer = userRepository.findUserByIdWithThrow(trainerId);
        if (!trainer.getRole().equals(UserRoleType.TRAINER)) {
            throw new PTException(PTErrorCode.NOT_TRAiNER);
        }

        // PT 정보를 가져옴
        PT pt = ptRepository.findPTByIdWithThrow(ptId);

        // 업데이트할 필드 값들
        String title = req.title();
        PTSpecialization specialization = req.specialization();
        LocalDateTime scheduledDate = req.scheduledDate();
        Long price = req.price();
        String description = req.description();
        Double latitude = req.latitude();
        Double longitude = req.longitude();

        // PT의 제목을 업데이트
        if (title != null && !title.isEmpty()) {
            pt.updateTitle(title);
        }

        // 전문 분야(카테고리) 업데이트
        if (specialization != null) {
            pt.updateSpecialization(specialization);
        }

        // 예약 날짜 업데이트
        if (scheduledDate != null) {
            pt.updateScheduledDate(scheduledDate);
        }

        // 가격 업데이트
        if (price != null) {
            pt.updatePrice(price);
        }

        // 설명 업데이트
        if (description != null && !description.isEmpty()) {
            pt.updateDescription(description);
        }

        if (latitude != null) {
            pt.updateLatitude(latitude);
        }

        if (longitude != null) {
            pt.updateLongitude(longitude);
        }

        // 업데이트된 PT 정보 저장
        ptRepository.save(pt);

        // 성공적으로 업데이트된 결과 반환
        return new PTUpdateRes("수정 완료");
    }

    public PTDeleteRes ptDelete(Long ptId, Long trainerId) {
        User trainer = userRepository.findUserByIdWithThrow(trainerId);

        if(!trainer.getRole().equals(UserRoleType.TRAINER)){
            throw new PTException(PTErrorCode.NOT_TRAiNER);
        }

        PT pt = ptRepository.findPTByIdWithThrow(ptId);

        ptRepository.delete(pt);

        return  new PTDeleteRes("삭제 완료");
    }

    public PTReservRes ptReserv(Long ptId, Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);
        if(!user.getRole().equals(UserRoleType.USER)){
            throw new UserException(UserErrorCode.NOT_ROLE_USER);
        }
        PT pt = ptRepository.findPTByIdWithThrow(ptId);
        if(user.getPoint()<pt.getPrice()){
            throw new PointException(PointErrorCode.NOT_POINT);
        }
        if(pt.getStatus()!=PTStatus.UNRESERVED){
            throw new PTException(PTErrorCode.NOT_STATUS);
        }
        user.minusPoint(pt.getPrice());
        pt.updateUser(user);
        pt.updateStatus(PTStatus.SCHEDULED);
        return new PTReservRes("예약 완료 되었습니다.");
    }

    public List<PTsReadRes> searchPTs(String keyword, PTSpecialization category, String sortBy, boolean isAsc) {
        // 정렬 조건 설정
        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

        // Repository 호출 (UNRESERVED 상태만 필터링)
        List<PT> entities = ptRepository.searchByKeywordAndCategoryAndStatus(PTStatus.UNRESERVED, keyword, category, sort);

        // 포문으로 변환
        List<PTsReadRes> result = new ArrayList<>();
        for (PT entity : entities) {
            result.add(new PTsReadRes(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getSpecialization().getName(),
                    entity.getScheduledDate(),
                    entity.getPrice(),
                    entity.getStatus().getDescription()
            ));
        }
        return result;
    }

    public List<PTsReadRes> getMyPTs(User user, PTStatus status) {
        List<PT> ptList;

        // 역할에 따라 다른 쿼리 실행
        if (user.getRole() == UserRoleType.USER) {
            ptList = ptRepository.findByUserIdAndStatus(user.getId(), status);
        } else if (user.getRole() == UserRoleType.TRAINER) {
            ptList = ptRepository.findByTrainerIdAndStatus(user.getId(), status);
        } else {
            // 역할이 유효하지 않을 경우 빈 리스트 반환
            return Collections.emptyList();
        }

        // PT 목록을 응답 객체로 변환
        return convertToPTsReadRes(ptList);
    }

    // PT 엔티티 리스트를 응답 객체 리스트로 변환하는 메서드
    private List<PTsReadRes> convertToPTsReadRes(List<PT> ptList) {
        List<PTsReadRes> result = new ArrayList<>();
        for (PT entity : ptList) {
            result.add(new PTsReadRes(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getSpecialization().getName(),
                    entity.getScheduledDate(),
                    entity.getPrice(),
                    entity.getStatus().getDescription()
            ));
        }
        return result;
    }

    public PTCompletedRes ptCompleted(Long ptId, Long trainerId) {
        PT pt=ptRepository.findPTByIdWithThrow(ptId);
        if(!pt.getTrainer().getId().equals( trainerId )){
            throw new UserException(UserErrorCode.NOT_FOUND_USER);
        }
        // 상태 변경 전 검증
        if (pt.getStatus() == PTStatus.COMPLETED) {
            throw new PTException(PTErrorCode.NOT_STATUS);
        }

        pt.updateStatus(PTStatus.COMPLETED);

        return new PTCompletedRes("완료 처리 하였습니다.");
    }
}
