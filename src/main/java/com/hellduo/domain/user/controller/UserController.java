package com.hellduo.domain.user.controller;

import com.hellduo.domain.user.dto.response.UserLoginRes;
import com.hellduo.domain.user.dto.request.UserSignupReq;
import com.hellduo.domain.user.dto.request.UserLoginReq;
import com.hellduo.domain.user.dto.response.UserSignupRes;
import com.hellduo.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
