package com.hellduo.domain.admin.controller;


import com.hellduo.domain.admin.dto.request.UserUpdateReq;
import com.hellduo.domain.admin.dto.response.GetTrainerListRes;
import com.hellduo.domain.admin.dto.response.GetUserListRes;
import com.hellduo.domain.admin.dto.response.UserUpdateRes;
import com.hellduo.domain.admin.service.AdminService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminUserController
{
    private final AdminService adminService;

    @GetMapping("/userList")
    public ResponseEntity<List<GetUserListRes>> getUserList(@AuthenticationPrincipal UserDetailsImpl userdetails)
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getUserList(userdetails.getUser()));
    }
    @GetMapping("/trainerList")
    public ResponseEntity<List<GetTrainerListRes>> getTrainerList(@AuthenticationPrincipal UserDetailsImpl userdetails)
    {
        return  ResponseEntity.status(HttpStatus.OK).body(adminService.getTrainerList(userdetails.getUser()));
    }
    @PutMapping("/userUpdate")
    public ResponseEntity<UserUpdateRes> userUpdate(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestBody UserUpdateReq req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.userUpdate(userDetails.getUser(),req));
    }

}
