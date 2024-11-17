package com.hellduo.domain.trainer.controller;

import com.hellduo.domain.user.dto.request.TrainerSignupReq;
import com.hellduo.domain.user.dto.response.TrainerSignupRes;
import com.hellduo.domain.user.service.TrainerService;
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
@RequestMapping("/api/v1/trainers")
public class TrainerController {
    private final TrainerService trainerService;

    @PostMapping("/signup")
    public ResponseEntity<TrainerSignupRes> signup(@Valid @RequestBody TrainerSignupReq req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trainerService.signup(req));
    }
}
