package com.hellduo.domain.admin.service;

import com.hellduo.domain.admin.dto.request.NoticeUpdateReq;
import com.hellduo.domain.admin.dto.response.*;
import com.hellduo.domain.admin.dto.request.NoticeReq;
import com.hellduo.domain.admin.entity.Notice;
import com.hellduo.domain.admin.repository.NoticeRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
                .user(user)
                .build();
        noticeRepository.save(notice); //
        return new NoticeRes("공지사항이 생성되었습니다.");

    }

    public List<GetNoticeListRes> getNoticeList()
    {
        List<Notice> noticeList = noticeRepository.findAll();
        List<GetNoticeListRes> noticeResList = new ArrayList<>();
        for (Notice notice : noticeList)
        {
            noticeResList.add(new GetNoticeListRes(notice.getTitle()));
        }
        return noticeResList;
    }

    public GetNoticeRes getNotice(Long noticeId)
    {
        Notice notice = noticeRepository.findNoticeByIdWithThrow(noticeId);
        return new GetNoticeRes(notice.getTitle(),
                                notice.getContent(),
                                notice.getUser().getId() ,
                                notice.getId());
    }

    public UpdateNoticeRes updateNotice(User user, Long noticeId, NoticeUpdateReq req)
    {
        if(user.getRole() != UserRoleType.ADMIN)
        {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        Notice notice = noticeRepository.findNoticeByIdWithThrow(noticeId);
        if (req.title() != null)
        {
            notice.updateTitle(req.title());
        }
        if (req.content() != null)
        {
            notice.updateContent(req.content());
        }
        return new UpdateNoticeRes("공지사항 수정완료");
    }

    public DeleteNoticeRes deleteNotice(User user, Long id)
    {
//        if (user.getRole() != UserRoleType.ADMIN)
//        {
//            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
//        }
        noticeRepository.deleteById(id);
        return new DeleteNoticeRes("공지사항이 삭제되었습니다.");
    }
}
