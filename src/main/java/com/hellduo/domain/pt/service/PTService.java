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
                build();

        ptRepository.save(pt);

        return new PTCreateRes("PT가 생성 되었습니다.");
    }

    public PTReadRes ptRead(Long ptId) {
        PT pt = ptRepository.findPTByIdWithThrow(ptId);
        return new PTReadRes(pt.getId(),
                pt.getScheduledDate(),
                pt.getPrice(),
                pt.getDescription(),
                pt.getTrainer().getName(),
                pt.getUser() != null ? pt.getUser().getName() : "미예약",
                pt.getStatus().name());
    }

    public List<PTsReadRes> ptsRead() {
        List<PT> pts = ptRepository.findAll();

        List<PTsReadRes> ptsReadResList = new ArrayList<>();
        for(PT pt: pts){
            ptsReadResList.add(new PTsReadRes(pt.getId(),
                    pt.getTitle(),
                    pt.getSpecialization(),
                    pt.getScheduledDate(),
                    pt.getPrice(),
                    pt.getStatus()));
        }
        return ptsReadResList;
    }

    public PTUpdateRes ptUpdate(Long ptId, PTUpdateReq req, Long trainerId) {
        User trainer = userRepository.findUserByIdWithThrow(trainerId);

        if(!trainer.getRole().equals(UserRoleType.TRAINER)){
            throw new PTException(PTErrorCode.NOT_TRAiNER);
        }

        PT pt = ptRepository.findPTByIdWithThrow(ptId);

        String title=req.title();
        PTSpecialization specialization=req.specialization();
        LocalDateTime scheduledDate=req.scheduledDate();
        Integer price=req.price();
        String description=req.description();

        if (title != null && !title.isEmpty()) {
            pt.updateTitle(title);
        }

        // 카테고리(전문 분야) 업데이트
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
}
