package net.skhu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net.skhu.model.Pagination;
import net.skhu.service.ArticleService;
import net.skhu.service.BoardService;

@Controller
@RequestMapping("article")
public class ArticleController {

    @Autowired BoardService boardService;
    @Autowired ArticleService articleService;

    @RequestMapping("list")
    public String list(Model model, Pagination pagination) {
        model.addAttribute("board", boardService.findById(pagination.getBd()));
        model.addAttribute("articles", articleService.findAll(pagination));
        return "article/list";
    }

    @RequestMapping("detail")
    public String detail(Model model, int id, Pagination pagination) {
        model.addAttribute("article", articleService.findById(id));
        return "article/detail";
    }
}
