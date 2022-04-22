package net.skhu.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.skhu.model.ArticleEdit;
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
        model.addAttribute("board", boardService.findById(pagination.getBd()));
        model.addAttribute("article", articleService.findById(id));
        return "article/detail";
    }

    @GetMapping("create")
    public String create(Model model, Pagination pagination) {
        model.addAttribute("board", boardService.findById(pagination.getBd()));
        model.addAttribute("articleEdit", new ArticleEdit());
        return "article/create";
    }

    @PostMapping("create")
    public String create(Model model, Pagination pagination,
            @Valid ArticleEdit articleEdit, BindingResult bindingResult ) {
        try {
            if (bindingResult.hasErrors() == false) {
                int id = articleService.insert(pagination.getBd(), articleEdit);
                return "redirect:detail?id=" + id + "&" + pagination.getQueryString();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            bindingResult.reject(null, "저장할 수 없습니다.");
        }
        model.addAttribute("board", boardService.findById(pagination.getBd()));
        return "article/create";
    }
 }
