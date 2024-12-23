package com.hellduo.domain.admin.controller;

import com.hellduo.domain.admin.dto.request.NoticeUpdateReq;
import com.hellduo.domain.admin.dto.response.*;
import com.hellduo.domain.admin.dto.request.NoticeReq;
import com.hellduo.domain.admin.service.AdminService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v2/admin")
@RestController
public class AdminNoticeController
{
    private final AdminService adminService;
    @PostMapping("/notice")
    public ResponseEntity<NoticeRes> createNotice(@RequestBody NoticeReq req,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createNotice(req,userDetails.getUser()));
    }

    @GetMapping("/notice")
    public ResponseEntity<List<GetNoticeListRes>> getNoticeList()
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getNoticeList());
    }

    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<GetNoticeRes> getNotice(@PathVariable Long noticeId)
    {
        return  ResponseEntity.status(HttpStatus.OK).body((adminService.getNotice(noticeId)));
    }
    @PutMapping("/notice/{noticeId}")
    public ResponseEntity<UpdateNoticeRes> updateNotice(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable Long noticeId,
                                                        @RequestBody NoticeUpdateReq req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateNotice(userDetails.getUser(),noticeId,req));
    }
    @DeleteMapping("/notice/{noticeId}")
    public ResponseEntity<DeleteNoticeRes> deleteNotice(@PathVariable Long noticeId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.deleteNotice(userDetails.getUser(),noticeId));
    }
}
