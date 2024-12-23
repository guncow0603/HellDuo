package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v2/page")
public class AdminPageController {
    @GetMapping("/banner")
    public String bannerPage() {
        return "admin/banner-create";
    }

    @GetMapping("/noticeList")
    public String noticeListPage() {
        return "admin/notice-list";
    }

    @GetMapping("/notice/{noticeId}")
    public String noticePage(@PathVariable Long noticeId, Model model) {
        model.addAttribute("noticeId", noticeId);
        return "admin/notice-read";
    }

    @GetMapping("/noticeCreate")
    public String noticeCreatePage() {
        return "admin/notice-create";
    }

    @GetMapping("/noticeUpdate/{noticeId}")
    public String noticeUpdatePage(@PathVariable Long noticeId, Model model) {
        model.addAttribute("noticeId", noticeId);
        return "admin/notice-update";
    }

    @GetMapping("/userList")
    public String userListPage(){
        return "admin/user-list";
    }

    @GetMapping("/reportCreate/{userId}")
    public String reportCreatePage(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "admin/report-create";
    }

    @GetMapping("/reportList")
    public String reportListPage() {
        return "admin/report-list";
    }
}
