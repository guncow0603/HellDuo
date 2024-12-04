package com.hellduo.domain.admin.repository;

import com.hellduo.domain.admin.entity.Notice;
import com.hellduo.domain.admin.exception.AdminException;
import com.hellduo.domain.admin.exception.NoticeErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>
{
    default Notice findNoticeByIdWithThrow(Long noticeId)
    {
        return findById(noticeId).
                orElseThrow(()->new AdminException(NoticeErrorCode.NOT_FOUND_NOTICE));
    };
}
