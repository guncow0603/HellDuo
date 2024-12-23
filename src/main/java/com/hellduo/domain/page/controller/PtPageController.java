package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v2/page")
public class PtPageController {
    @GetMapping("/ptCreate")
    public String ptCreatePage() {
        return "pt/pt-create";
    }

    @GetMapping("/ptList")
    public String ptListPage() {
        return "pt/pt-list";
    }

    @GetMapping("/ptRead/{ptId}")
    public String ptReadPage(@PathVariable Long ptId, Model model) {
        model.addAttribute("ptId", ptId);
        return "pt/pt-read"; // pt-read.html 페이지로 이동
    }

    @GetMapping("/ptUpdate/{ptId}")
    public String ptUpdatePage(@PathVariable Long ptId, Model model) {
        model.addAttribute("ptId", ptId);
        return "pt/pt-update";
    }

    @GetMapping("/myPt")
    public String myPtPage() {
        return "pt/my-pt";
    }
}
