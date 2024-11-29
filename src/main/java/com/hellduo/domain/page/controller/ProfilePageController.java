package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/page")
public class ProfilePageController {
    @GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }

    @GetMapping("/profile-edit")
    public String profileEditPage() {
        return "profile-edit";
    }
}
