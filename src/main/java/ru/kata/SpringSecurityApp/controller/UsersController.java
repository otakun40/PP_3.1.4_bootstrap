package ru.kata.SpringSecurityApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.SpringSecurityApp.model.Role;
import ru.kata.SpringSecurityApp.service.RolesService;
import ru.kata.SpringSecurityApp.model.User;
import ru.kata.SpringSecurityApp.service.UsersService;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping()
public class UsersController {

    private final UsersService usersService;
    private final RolesService rolesService;

    @Autowired
    public UsersController(UsersService usersService, RolesService rolesService) {
        this.usersService = usersService;
        this.rolesService = rolesService;
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "registration_user";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors() ||
                usersService.getUserByUsername(user.getUsername()) != null) {
            return "registration_user";
        }
        Role role = rolesService.getRoleByName("ROLE_USER");
        user.setRoles(new HashSet<>(Set.of(role)));
        usersService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        Long id = usersService.getUserByUsername(principal.getName()).getId();
        model.addAttribute("user", usersService.getUserById(id));
        return "show_user";
    }
}
