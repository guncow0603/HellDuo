package com.hellduo.domain.admin.service;

import com.hellduo.domain.admin.dto.request.NoticeUpdateReq;
import com.hellduo.domain.admin.dto.request.UserUpdateReq;
import com.hellduo.domain.admin.dto.response.*;
import com.hellduo.domain.admin.dto.request.NoticeReq;
import com.hellduo.domain.admin.entity.Notice;
import com.hellduo.domain.user.entity.enums.UserStatus;
import com.hellduo.domain.admin.repository.NoticeRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public NoticeRes createNotice(NoticeReq req, User user) {
        if (user.getRole() != UserRoleType.ADMIN) {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        Notice notice = Notice.builder()
                .title(req.title())
                .content(req.content())
                .user(user)
                .build();
        noticeRepository.save(notice);
        return new NoticeRes("공지사항이 생성되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<GetNoticeListRes> getNoticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        List<GetNoticeListRes> noticeResList = new ArrayList<>();
        for (Notice notice : noticeList) {
            noticeResList.add(new GetNoticeListRes(notice.getTitle(), notice.getId()));
        }
        return noticeResList;
    }

    @Transactional(readOnly = true)
    public GetNoticeRes getNotice(Long noticeId) {
        Notice notice = noticeRepository.findNoticeByIdWithThrow(noticeId);
        return new GetNoticeRes(notice.getTitle(),
                notice.getContent(),
                notice.getUser().getId(),
                notice.getId());
    }

    @Transactional
    public UpdateNoticeRes updateNotice(User user, Long noticeId, NoticeUpdateReq req) {
        if (user.getRole() != UserRoleType.ADMIN) {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        Notice notice = noticeRepository.findNoticeByIdWithThrow(noticeId);
        if (req.title() != null) {
            notice.updateTitle(req.title());
        }
        if (req.content() != null) {
            notice.updateContent(req.content());
        }
        return new UpdateNoticeRes("공지사항 수정완료");
    }

    @Transactional
    public DeleteNoticeRes deleteNotice(User user, Long id) {
        if (user.getRole() != UserRoleType.ADMIN) {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        noticeRepository.deleteById(id);
        return new DeleteNoticeRes("공지사항이 삭제되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<GetUserListRes> getUserList(User user) {
        if (user.getRole() != UserRoleType.ADMIN) {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        List<User> userList = userRepository.findAll();
        List<GetUserListRes> userResList = new ArrayList<>();

        for (User user1 : userList) {
            if (user1.getRole() == UserRoleType.USER) {
                userResList.add(new GetUserListRes(user1.getId(),
                        user1.getName(),
                        user1.getEmail(),
                        user1.getGender().getDescription(),
                        user1.getAge(),
                        user1.getPhoneNumber(),
                        user1.getNickname(),
                        user1.getWeight(),
                        user1.getHeight()));
            }
        }
        return userResList;
    }

    @Transactional(readOnly = true)
    public List<GetTrainerListRes> getTrainerList(User user) {
        if (user.getRole() != UserRoleType.ADMIN) {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        List<User> userList = userRepository.findAll();
        List<GetTrainerListRes> trainerResList = new ArrayList<>();
        for (User user1 : userList) {
            if (user1.getRole() == UserRoleType.TRAINER) {
                trainerResList.add(new GetTrainerListRes(user1.getId(),
                        user1.getEmail(),
                        user1.getName(),
                        user1.getPhoneNumber(),
                        user1.getGender().getDescription(),
                        user1.getAge(),
                        user1.getSpecialization().getName(),
                        user1.getExperience(),
                        user1.getCertifications(),
                        user1.getBio()));
            }
        }
        return trainerResList;
    }

    public UserUpdateRes userUpdate(User admin, UserUpdateReq req) {
        if (admin.getRole() != UserRoleType.ADMIN) {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        User user = userRepository.findUserByIdWithThrow(req.userId());
        if (req.userstatus().equals(UserStatus.DELETED)) {
            user.updateUserStatus(UserStatus.DELETED);
        } else if (req.userstatus().equals(UserStatus.REST)) {
            user.updateUserStatus(UserStatus.REST);
        }
        else if (req.userstatus().equals(UserStatus.ACTION)){
            user.updateUserStatus(UserStatus.ACTION);
        }
        return new UserUpdateRes("유저상태 변경완료");
    }
}