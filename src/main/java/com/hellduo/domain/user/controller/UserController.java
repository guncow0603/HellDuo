package com.hellduo.domain.user.controller;

import com.hellduo.domain.user.dto.request.*;
import com.hellduo.domain.user.dto.response.*;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.service.UserService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping
    public ResponseEntity<UserProfileUpdateRes> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserProfileUpdateReq req){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUserProfile(userDetails.getUser().getId(),req));
    }

    @PutMapping("/trainer")
    public ResponseEntity<TrainerProfileUpdateRes> updateTrainerProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody TrainerProfileUpdateReq req){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateTrainerProfile(userDetails.getUser().getId(),req));
    }

    @PostMapping("/logout")
    public ResponseEntity<UserLogoutRes> logout(HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.logout(response));
    }

    @PutMapping("/withdrawal")
    public ResponseEntity<UserWithdrawalRes> withdrawal(@RequestBody UserWithdrawalReq req,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.withdrawal(req, userDetails.getUser().getId(),response));
    }

    @GetMapping("/role")
    public UserRoleType getUserRole(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUser().getRole();
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<TrainerOwnProfileGetRes> getTrainerProfile(
            @PathVariable Long trainerId){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTrainerProfile(trainerId));
    }

    @GetMapping("/trainer/bestRating")
    public ResponseEntity<List<BestRatingTrainerRes>>getBestRatingTrainer() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getBestRatingTrainer());
    }
}
