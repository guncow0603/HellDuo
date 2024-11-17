package com.hellduo.domain.user.controller;

import com.hellduo.domain.user.dto.response.UserLoginRes;
import com.hellduo.domain.user.dto.request.UserSignupReq;
import com.hellduo.domain.user.dto.request.UserLoginReq;
import com.hellduo.domain.user.dto.response.UserOwnProfileGetRes;
import com.hellduo.domain.user.dto.response.UserSignupRes;
import com.hellduo.domain.user.service.UserService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupRes> signup(@Valid @RequestBody UserSignupReq req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRes> login(@RequestBody UserLoginReq req,
                                              HttpServletResponse res) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.login(req, res));
    }

    @GetMapping
    public ResponseEntity<UserOwnProfileGetRes> getOwnProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(userService.getOwnProfile(userDetails.getUser().getId()));
    }

}
