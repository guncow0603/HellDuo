package com.hellduo.domain.pt.controller;

import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.dto.response.PTCreateRes;
import com.hellduo.domain.pt.dto.response.PTReadRes;
import com.hellduo.domain.pt.dto.response.PTsReadRes;
import com.hellduo.domain.pt.service.PTService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pt")
public class PTController {
    private final PTService ptService;
    @PostMapping
    public ResponseEntity<PTCreateRes> ptCreate(@RequestBody PTCreateReq req,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(ptService.ptCreate(req, userDetails.getUser().getId()));

    }

    @GetMapping("/{ptId}")
    public  ResponseEntity<PTReadRes> ptRead (@PathVariable Long ptId){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptRead(ptId));
    }

    @GetMapping
    public  ResponseEntity<List<PTsReadRes>> ptsRead (){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptsRead());
    }
}
