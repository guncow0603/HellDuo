package com.hellduo.domain.user.controller;

import com.hellduo.domain.user.dto.request.*;
import com.hellduo.domain.user.dto.response.*;
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

    @PostMapping("/trainerSignup")
    public ResponseEntity<TrainerSignupRes> signup(@Valid @RequestBody TrainerSignupReq req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.trainerSignup(req));
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
    @GetMapping("/trainer")
    public ResponseEntity<TrainerOwnProfileGetRes> getOwnTrainerProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(userService.getOwnTrainerProfile(userDetails.getUser().getId()));
    }

}
