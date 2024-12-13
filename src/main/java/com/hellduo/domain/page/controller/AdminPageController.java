package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/page")
public class AdminPageController {
    @GetMapping("/banner")
    public String bannerPage() {
        return "banner";
    }

    @GetMapping("/noticeList")
    public String noticeListPage() {
        return "notice-list";
    }

    @GetMapping("/notice/{noticeId}")
    public String noticePage(@PathVariable Long noticeId, Model model) {
        model.addAttribute("noticeId", noticeId);
        return "notice-read";
    }

    @GetMapping("/noticeCreate")
    public String noticeCreatePage() {
        return "notice-create";
    }

    @GetMapping("/noticeUpdate/{noticeId}")
    public String noticeUpdatePage(@PathVariable Long noticeId, Model model) {
        model.addAttribute("noticeId", noticeId);
        return "notice-update";
    }
}
