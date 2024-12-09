package com.hellduo.domain.pt.controller;

import com.hellduo.domain.pt.dto.request.PTUpdateReq;
import com.hellduo.domain.pt.dto.response.*;
import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.entity.PTSpecialization;
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
    public ResponseEntity<PTReadRes> ptRead (@PathVariable Long ptId){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptRead(ptId));
    }

    @GetMapping
    public ResponseEntity<List<getPTsRes>> ptsRead(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptsRead(latitude, longitude));
    }

    @PutMapping("/{ptId}")
    public ResponseEntity<PTUpdateRes> ptUpdate (@PathVariable Long ptId,
                                                  @RequestBody PTUpdateReq req,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptUpdate(ptId,req,userDetails.getUser().getId()));
    }

    @DeleteMapping("/{ptId}")
    public ResponseEntity<PTDeleteRes> ptDelete (@PathVariable Long ptId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptDelete(ptId,userDetails.getUser().getId()));
    }

    @PatchMapping("/{ptId}")
    public ResponseEntity<PTReservRes> ptReserv (@PathVariable Long ptId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptReserv(ptId,userDetails.getUser().getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PTsReadRes>> searchPTs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) PTSpecialization category,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc)
    {
        return ResponseEntity.status(HttpStatus.OK).body(ptService.searchPTs(keyword, category,sortBy,isAsc));
    }
}
