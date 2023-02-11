package ru.kata.SpringSecurityApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.SpringSecurityApp.service.RolesService;
import ru.kata.SpringSecurityApp.model.User;
import ru.kata.SpringSecurityApp.service.UsersService;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsersService usersService;
    private final RolesService rolesService;

    @Autowired
    public AdminController(UsersService usersService, RolesService rolesService) {
        this.usersService = usersService;
        this.rolesService = rolesService;
    }

    @GetMapping
    public String getAdminPage(Model model, Principal principal) {
        Long id = usersService.getUserByEmail(principal.getName()).getId();
        model.addAttribute("user", usersService.getUserById(id));
        model.addAttribute("users", usersService.getAllUsers());
        model.addAttribute("roles", rolesService.getRoles());
        return "admin/admin_page";
    }

    @GetMapping("/new")
    public String newUserPage(@ModelAttribute("newUser") User user, Model model, Principal principal) {
        model.addAttribute("user", usersService.getUserById(
                usersService.getUserByEmail(principal.getName()).getId()));
        model.addAttribute("roles", rolesService.getRoles());
        return "admin/new_user";
    }

    @PostMapping("/add")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @RequestParam(required = false, value = "new_roles") String[] stringRole) {
        if (bindingResult.hasErrors() ||
                usersService.getUserByEmail(user.getEmail()) != null) {
            return ("admin/new_user");
        }
        setRoles(user, stringRole);
        usersService.saveUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}/update")
    public String updateUser(@ModelAttribute("currentUser") User user,
                             @RequestParam(required = false, value = "update_roles") String[] stringRole) {
        setRoles(user, stringRole);
        usersService.updateUser(user);
        return "redirect:/admin";
    }

    private void setRoles(@ModelAttribute("currentUser") User user, @RequestParam(required = false, value = "update_roles") String[] stringRole) {
        if (stringRole.length == 1) {
            user.setRoles(new HashSet<>(Set.of(rolesService.getRoleByName(
                    stringRole[0].equals("ADMIN")? "ROLE_ADMIN" : "ROLE_USER"))));
        } else {
            user.setRoles(new HashSet<>(Set.of(rolesService.getRoleByName("ROLE_ADMIN"),
                    rolesService.getRoleByName("ROLE_USER"))));
        }
    }

    @DeleteMapping("/{id}/delete")
    public String removeUserById(@PathVariable("id") Long id) {
        usersService.removeUserById(id);
        return "redirect:/admin";
    }
}
