package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/page")
public class AuthPageController {
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/trainer-signup")
    public String trainerSignupPage() {
        return "trainer-signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
}
