package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/page")
public class ReviewPageController {
    @GetMapping("/reviewList")
    public String reviewListPage() {
        return "review-list";
    }

    @GetMapping("/reviewCreate/{ptId}")
    public String reviewCreatePage(@PathVariable Long ptId, Model model) {
        model.addAttribute("ptId", ptId);
        return "review-create"; // pt-read.html 페이지로 이동
    }


}
