package com.hellduo.domain.admin.controller;


import com.hellduo.domain.admin.dto.response.GetNoticeListRes;
import com.hellduo.domain.admin.dto.response.GetUserListRes;
import com.hellduo.domain.admin.service.AdminService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminUserController
{
    private final AdminService adminService;

    @GetMapping("/userList")
    public ResponseEntity<List<GetUserListRes>> getNoticeList(@AuthenticationPrincipal UserDetailsImpl userdetails)
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getUserList(userdetails.getUser()));
    }
}
