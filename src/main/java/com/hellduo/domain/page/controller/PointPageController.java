package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/page")
public class PointPageController {
    @GetMapping("/point-charge")
    public String pointChargePage() {
        return "payment/point-charge";
    }

    @GetMapping("/success")
    public String successPage() {
        return "payment/success";
    }

    @GetMapping("/fail")
    public String failPage() {
        return "payment/fail";
    }

    @GetMapping("/point-charge-list")
    public String pointChargeListPage() {
        return "payment/point-charge-list";
    }

}
