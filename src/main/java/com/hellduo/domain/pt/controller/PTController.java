package com.hellduo.domain.pt.controller;

import com.hellduo.domain.board.dto.response.BoardsReadRes;
import com.hellduo.domain.board.dto.response.PageBoardsRes;
import com.hellduo.domain.pt.dto.request.PTUpdateReq;
import com.hellduo.domain.pt.dto.response.*;
import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.entity.enums.PTSpecialization;
import com.hellduo.domain.pt.entity.enums.PTStatus;
import com.hellduo.domain.pt.service.PTService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/pt")
public class PTController {
    private final PTService ptService;
    @PostMapping
    public ResponseEntity<PTCreateRes> ptCreate(@RequestBody PTCreateReq req,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(ptService.ptCreate(req, userDetails.getUser()));

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
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptUpdate(ptId,req,userDetails.getUser()));
    }

    @DeleteMapping("/{ptId}")
    public ResponseEntity<PTDeleteRes> ptDelete (@PathVariable Long ptId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptDelete(ptId,userDetails.getUser()));
    }

    @PatchMapping("/{ptId}")
    public ResponseEntity<PTReservRes> ptReserv (@PathVariable Long ptId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptReserv(ptId,userDetails.getUser()));
    }

    @GetMapping("/search")
    public ResponseEntity<PagePTsRes> searchPTs(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) PTSpecialization category)
    {
        Page<PTsReadRes> pageResult = ptService.searchPTs(page - 1, size, sortBy, isAsc, keyword, category);
        List<PTsReadRes> pts = pageResult.getContent();  // 실제 데이터 추출

        // PagedPTsRes 객체 생성하여 반환
        PagePTsRes pagePTsRes = new PagePTsRes(
                pts,
                pageResult.getTotalPages(),  // 전체 페이지 수
                pageResult.getNumber()    // 현재 페이지 번호 (1부터 시작하도록 +1)
        );

        return ResponseEntity.status(HttpStatus.OK).body(pagePTsRes);

    }

    

    @GetMapping("/myPt")
    public ResponseEntity<List<PTsReadRes>> getMyPTs(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "SCHEDULED") PTStatus status)
    {
        return ResponseEntity.status(HttpStatus.OK).body(ptService.getMyPTs(userDetails.getUser(), status));
    }

    @PatchMapping("/completed/{ptId}")
    public ResponseEntity<PTCompletedRes> ptCompleted (
            @PathVariable Long ptId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(ptService.ptCompleted(ptId,userDetails.getUser()));
    }

    @GetMapping("/completedPTs")
    public ResponseEntity<List<PTsReadRes>> getCompletedPTs(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ptService.getCompletedPTs(userDetails.getUser()));
    }
}
