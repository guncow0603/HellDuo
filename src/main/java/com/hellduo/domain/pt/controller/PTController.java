package com.hellduo.domain.pt.controller;

import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.dto.response.PTCreateRes;
import com.hellduo.domain.pt.service.PTService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PTController {
    private final PTService ptService;
    @PostMapping("/pt")
    public ResponseEntity<PTCreateRes> PTCreate(@RequestBody PTCreateReq req,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(ptService.ptCreate(req, userDetails.getUser().getId()));

    }
}
