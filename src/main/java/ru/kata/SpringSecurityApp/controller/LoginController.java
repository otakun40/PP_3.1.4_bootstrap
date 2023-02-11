package ru.kata.SpringSecurityApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/")
    public String startPage() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error != null);
        return "login";
    }
}
