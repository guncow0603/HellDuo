package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/page")
public class PtPageController {
    @GetMapping("/ptCreate")
    public String ptCreatePage() {
        return "pt-create";
    }

    @GetMapping("/ptList")
    public String ptListPage() {
        return "pt-list";
    }
}
