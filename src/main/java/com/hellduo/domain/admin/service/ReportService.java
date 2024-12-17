package com.hellduo.domain.admin.service;

import com.hellduo.domain.admin.dto.request.UserReportReq;
import com.hellduo.domain.admin.dto.response.GetReportListRes;
import com.hellduo.domain.admin.dto.response.UserReportRes;
import com.hellduo.domain.admin.entity.Report;
import com.hellduo.domain.admin.repository.ReportRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    public UserReportRes reportCreate(User reporterUser, UserReportReq req) {
        User reporteduser = userRepository.findUserByIdWithThrow(req.reportedUserId());
        Report report = Report.builder().
                content(req.content()).
                reportReason(req.reportReason()).
                reportedUser(reporteduser).
                reporterUser(reporterUser).
                build();
        reportRepository.save(report);
        return new UserReportRes("신고가 완료되었습니다.");
    }

    public List<GetReportListRes> getReportList(User user) {
        if (!user.getRole().equals(UserRoleType.ADMIN))
        {
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        List<Report> reportList = reportRepository.findAll();
        List<GetReportListRes> getReportListRes = new ArrayList<>();
        for (Report report : reportList) {
            getReportListRes.add(
                    new GetReportListRes(
                            report.getReporterUser().getEmail(),
                            report.getReportedUser().getEmail(),
                            report.getReportReason().getReportReason(),
                            report.getContent(),
                            report.getReportedUser().getId(),
                            report.getReportedUser().getUserStatus()));
        }
        return getReportListRes;
    }
}

