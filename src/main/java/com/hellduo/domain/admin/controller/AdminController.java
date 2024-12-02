package com.hellduo.domain.admin.controller;

import com.hellduo.domain.admin.dto.reponse.NoticeRes;
import com.hellduo.domain.admin.dto.request.NoticeReq;
import com.hellduo.domain.admin.service.AdminService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController
{
    private final AdminService adminService;
    @PostMapping("/notice")
    public ResponseEntity<NoticeRes> createNotice(@RequestBody NoticeReq req,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createNotice(req,userDetails.getUser()));
    }

}
