package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v2/page")
public class ReviewPageController {
    @GetMapping("/reviewList/{trainerId}")
    public String reviewListPage(@PathVariable Long trainerId, Model model) {
        model.addAttribute("trainerId", trainerId);
        return "review/trainer-review-list";
    }
    @GetMapping("/reviewList")
    public String reviewListPage() {
        return "review/review-list";
    }

    @GetMapping("/reviewCreate/{ptId}")
    public String reviewCreatePage(@PathVariable Long ptId, Model model) {
        model.addAttribute("ptId", ptId);
        return "review/review-create";
    }

    @GetMapping("/noReviewPtList")
    public String noReviewPtListPage() {
        return "review/no-review-pt-list";
    }

    @GetMapping("/reviewRead/{reviewId}")
    public String reviewReadPage(@PathVariable Long reviewId, Model model) {
        model.addAttribute("reviewId", reviewId);
        return "review/review-read";
    }
}
