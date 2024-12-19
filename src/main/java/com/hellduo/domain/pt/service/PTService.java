package com.hellduo.domain.pt.service;

import com.hellduo.domain.imageFile.entity.ImageFile;
import com.hellduo.domain.imageFile.service.ImageFileService;
import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.dto.request.PTUpdateReq;
import com.hellduo.domain.pt.dto.response.*;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.enums.PTSpecialization;
import com.hellduo.domain.pt.entity.enums.PTStatus;
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
import com.hellduo.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class PTService {
    private final PTRepository ptRepository;
    private final ImageFileService imageFileService;

    @Transactional
    public PTCreateRes ptCreate(PTCreateReq req, User trainer) {
        if (!trainer.getRole().equals(UserRoleType.TRAINER)) {
            throw new PTException(PTErrorCode.NOT_TRAiNER);
        }

        PT pt = PT.builder()
                .title(req.title())
                .specialization(req.specialization())
                .trainer(trainer)
                .scheduledDate(req.scheduledDate())
                .price(req.price())
                .description(req.description())
                .status(PTStatus.UNRESERVED)
                .latitude(req.latitude())
                .longitude(req.longitude())
                .address(req.address())
                .build();

        ptRepository.save(pt);

        return new PTCreateRes(pt.getId(), "PT가 생성 되었습니다.");
    }

    @Transactional(readOnly = true)
    public PTReadRes ptRead(Long ptId) {
        PT pt = ptRepository.findPTByIdWithThrow(ptId);

        // User가 null인 경우를 처리하도록 수정
        Long userId = (pt.getUser() != null) ? pt.getUser().getId() : null;
        String userName = (pt.getUser() != null) ? pt.getUser().getName() : "미예약";

        return new PTReadRes(
                pt.getId(),
                pt.getTrainer().getId(),
                pt.getTitle(),
                pt.getScheduledDate(),
                pt.getPrice(),
                pt.getDescription(),
                pt.getTrainer().getName(),
                pt.getSpecialization().getName(),
                userName, // "미예약"을 기본 값으로 설정
                pt.getStatus().getDescription(),
                pt.getLatitude(),
                pt.getLongitude(),
                userId // null이 될 수 있음
        );
    }

    @Transactional(readOnly = true)
    public List<getPTsRes> ptsRead(Double userLatitude, Double userLongitude) {
        List<PT> pts = ptRepository.findByStatus(PTStatus.UNRESERVED);
        pts.sort(Comparator.comparingDouble(pt -> calculateDistance(userLatitude, userLongitude, pt.getLatitude(), pt.getLongitude())));

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

    @Transactional
    public PTUpdateRes ptUpdate(Long ptId, PTUpdateReq req, User trainer) {
        PT pt = ptRepository.findPTByIdWithThrow(ptId);

        if (!pt.getTrainer().getId().equals(trainer.getId())) {
            throw new PTException(PTErrorCode.NOT_OWN_TRAINER);
        }

        String title = req.title();
        PTSpecialization specialization = req.specialization();
        LocalDateTime scheduledDate = req.scheduledDate();
        Long price = req.price();
        String description = req.description();
        Double latitude = req.latitude();
        Double longitude = req.longitude();

        if (title != null && !title.isEmpty()) {
            pt.updateTitle(title);
        }

        if (specialization != null) {
            pt.updateSpecialization(specialization);
        }

        if (scheduledDate != null) {
            pt.updateScheduledDate(scheduledDate);
        }

        if (price != null) {
            pt.updatePrice(price);
        }

        if (description != null && !description.isEmpty()) {
            pt.updateDescription(description);
        }

        if (latitude != null) {
            pt.updateLatitude(latitude);
        }

        if (longitude != null) {
            pt.updateLongitude(longitude);
        }

        return new PTUpdateRes("수정 완료");
    }

    @Transactional
    public PTDeleteRes ptDelete(Long ptId, User trainer) {
        PT pt = ptRepository.findPTByIdWithThrow(ptId);

        if (!pt.getTrainer().getId().equals(trainer.getId())&& !trainer.getRole().equals(UserRoleType.ADMIN)) {
            throw new PTException(PTErrorCode.NOT_OWN_TRAINER);
        }

        imageFileService.deleteImages(ptId,"pt");
        ptRepository.delete(pt);

        return new PTDeleteRes("삭제 완료");
    }

    @Transactional
    public PTReservRes ptReserv(Long ptId, User user) {
        if (!user.getRole().equals(UserRoleType.USER)) {
            throw new UserException(UserErrorCode.NOT_ROLE_USER);
        }
        PT pt = ptRepository.findPTByIdWithThrow(ptId);
        if (user.getPoint() < pt.getPrice()) {
            throw new PointException(PointErrorCode.NOT_POINT);
        }
        if (pt.getStatus() != PTStatus.UNRESERVED) {
            throw new PTException(PTErrorCode.NOT_STATUS);
        }
        user.minusPoint(pt.getPrice());
        pt.updateUser(user);
        pt.updateStatus(PTStatus.SCHEDULED);
        return new PTReservRes("예약 완료 되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<PTsReadRes> searchPTs(String keyword, PTSpecialization category, String sortBy, boolean isAsc) {
        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        List<PT> entities = ptRepository.searchByKeywordAndCategoryAndStatus(PTStatus.UNRESERVED, keyword, category, sort);

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

    @Transactional(readOnly = true)
    public List<PTsReadRes> getMyPTs(User user, PTStatus status) {
        List<PT> ptList;

        if (user.getRole() == UserRoleType.USER) {
            ptList = ptRepository.findByUserIdAndStatus(user.getId(), status);
        } else if (user.getRole() == UserRoleType.TRAINER) {
            ptList = ptRepository.findByTrainerIdAndStatus(user.getId(), status);
        } else {
            return Collections.emptyList();
        }

        return convertToPTsReadRes(ptList);
    }

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

    @Transactional
    public PTCompletedRes ptCompleted(Long ptId, User trainer) {
        PT pt = ptRepository.findPTByIdWithThrow(ptId);
        if (!pt.getTrainer().getId().equals(trainer.getId())) {
            throw new PTException(PTErrorCode.NOT_OWN_TRAINER);
        }

        if (pt.getStatus() == PTStatus.COMPLETED) {
            throw new PTException(PTErrorCode.NOT_STATUS);
        }

        pt.updateStatus(PTStatus.COMPLETED);

        return new PTCompletedRes("완료 처리 하였습니다.");
    }

    @Transactional(readOnly = true)
    public List<PTsReadRes> getCompletedPTs(User user) {
        if (user.getRole() != UserRoleType.USER) {
            throw new UserException(UserErrorCode.NOT_ROLE_USER);
        }
        List<PT> pts = ptRepository.findByUserIdAndStatusAndReviewIsNull(user.getId(), PTStatus.COMPLETED);

        List<PTsReadRes> PTsReadResList = new ArrayList<>();
        for (PT pt : pts) {
            PTsReadResList.add(new PTsReadRes(
                    pt.getId(),
                    pt.getTitle(),
                    pt.getSpecialization().getName(),
                    pt.getScheduledDate(),
                    pt.getPrice(),
                    pt.getStatus().getDescription()
            ));
        }
        return PTsReadResList;
    }
}