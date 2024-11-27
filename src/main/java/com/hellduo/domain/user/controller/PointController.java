package com.hellduo.domain.user.controller;

import com.hellduo.domain.user.dto.response.ChargePointRes;
import com.hellduo.domain.user.service.PointService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/point")
public class PointController {
    private final PointService pointService;
    @GetMapping("/charge")
    public ResponseEntity<List<ChargePointRes>> chargePointRead(
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(pointService.chargePointRead(userDetails.getUser().getId()));
    }
}
