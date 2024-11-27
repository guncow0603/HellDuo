package com.hellduo.domain.pt.service;

import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.dto.response.PTCreateRes;
import com.hellduo.domain.pt.dto.response.PTReadRes;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.PTStatus;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.UserRoleType;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
