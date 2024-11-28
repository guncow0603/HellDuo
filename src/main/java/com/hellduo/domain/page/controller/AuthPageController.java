package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AuthPageController {
    // 회원가입 페이지를 반환하는 메소드
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
}
