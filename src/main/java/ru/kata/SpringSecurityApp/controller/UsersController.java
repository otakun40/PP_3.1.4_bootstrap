package ru.kata.SpringSecurityApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.SpringSecurityApp.service.UsersService;
import java.security.Principal;



@Controller
@RequestMapping("/")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        Long id = usersService.getUserByEmail(principal.getName()).getId();
        model.addAttribute("user", usersService.getUserById(id));
        return "user/user_page";
    }
}
