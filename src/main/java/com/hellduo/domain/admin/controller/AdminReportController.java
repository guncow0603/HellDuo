package com.hellduo.domain.admin.controller;

import com.hellduo.domain.admin.dto.request.UserReportReq;
import com.hellduo.domain.admin.dto.response.GetReportListRes;
import com.hellduo.domain.admin.dto.response.UserReportRes;
import com.hellduo.domain.admin.service.ReportService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v2/report")
@RestController
public class AdminReportController {
    private final ReportService reportService;
    @PostMapping
    public ResponseEntity<UserReportRes> reportCreate(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestBody UserReportReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.reportCreate(userDetails.getUser(),req));
    }
    @GetMapping("/admin")
    public ResponseEntity<List<GetReportListRes>> getReportList(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getReportList(userDetails.getUser()));
    }
}
