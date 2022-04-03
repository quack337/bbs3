package net.skhu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.skhu.model.Pagination;
import net.skhu.service.UserService;

@Controller
@RequestMapping("user")
@Secured("ROLE_ADMIN")
public class UserController {
    @Autowired UserService userService;

    @RequestMapping("list")
    public String list(Model model, Pagination pagination) {
        model.addAttribute("users", userService.findAll(pagination));
        return "user/list";
    }

    @GetMapping("edit")
    public String edit(Model model, Pagination pagination, int id) {
        return "user/edit";
    }

}
