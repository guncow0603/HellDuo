package com.hellduo.domain.admin.service;

import com.hellduo.domain.admin.dto.reponse.NoticeRes;
import com.hellduo.domain.admin.dto.request.NoticeReq;
import com.hellduo.domain.admin.entity.Notice;
import com.hellduo.domain.admin.exception.AdminException;
import com.hellduo.domain.admin.exception.NoticeErrorCode;
import com.hellduo.domain.admin.repository.NoticeRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final NoticeRepository noticeRepository;
    public NoticeRes createNotice(NoticeReq req, User user)
    {
        if(user.getRole() != UserRoleType.ADMIN)
        {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        Notice notice = Notice.builder()
                .title(req.title())
                .content(req.content())
                .build();
        noticeRepository.save(notice);
        return new NoticeRes("공지사항이 생성되었습니다.");

    }
}
