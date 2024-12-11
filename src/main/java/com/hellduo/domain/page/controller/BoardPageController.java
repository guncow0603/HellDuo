package com.hellduo.domain.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/page")
public class BoardPageController {
    @GetMapping("/boardList")
    public String boardPage() {
        return "board-list";
    }

    @GetMapping("/boardUpdate/{boardId}")
    public String boardUpdatePage(@PathVariable Long boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "board-update";
    }

    @GetMapping("/boardCreate")
    public String boardCreatePage() {
        return "board-create";
    }

    @GetMapping("/boardRead/{boardId}")
    public String boardReadPage(@PathVariable Long boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "board-read";
    }

}
