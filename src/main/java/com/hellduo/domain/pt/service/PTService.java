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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                build();

        ptRepository.save(pt);

        return new PTCreateRes("PT가 생성 되었습니다.");
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

    public List<PTsReadRes> ptsRead() {
        List<PT> pts = ptRepository.findAll();

        List<PTsReadRes> ptsReadResList = new ArrayList<>();
        for(PT pt: pts){
            ptsReadResList.add(new PTsReadRes(pt.getId(),
                    pt.getTitle(),
                    pt.getSpecialization().getName(),
                    pt.getScheduledDate(),
                    pt.getPrice(),
                    pt.getStatus().getDescription()));
        }
        return ptsReadResList;
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
}
