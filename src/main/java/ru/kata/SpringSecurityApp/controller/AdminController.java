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

    @GetMapping()
    public String getAdminPage(Model model) {
        model.addAttribute("users", usersService.getAllUsers());
        return "admin_page";
    }

    @GetMapping("/new")
    public String getUserCreateForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", rolesService.getRoles());
        return "new_user";
    }

    @PostMapping("/createNew")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @RequestParam(required = false, value = "my_roles") String[] stringRole) {
        if (bindingResult.hasErrors() ||
                usersService.getUserByUsername(user.getUsername()) != null) {
            return ("/new_user");
        }
        if(stringRole != null){
            if (stringRole.length == 1) {
                user.setRoles(new HashSet<>(Set.of(rolesService.getRoleByName(stringRole[0]))));
            } else {
                user.setRoles(new HashSet<>(Set.of(rolesService.getRoleByName(stringRole[0]),
                        rolesService.getRoleByName(stringRole[1]))));
            }
        } else {
            user.setRoles(new HashSet<>(Set.of(rolesService.getRoleByName("ROLE_USER"))));
        }
        usersService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/{id}/edit")
    public String getUserEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", usersService.getUserById(id));
//        model.addAttribute("roles", rolesService.getRoles());
        return "edit_user";
    }

    @PatchMapping(value = "/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @PathVariable (name = "id") long id,
                             @RequestParam(required = false, value = "my_roles") String[] stringRole) {
        if (bindingResult.hasErrors())
            return ("/edit_user");
        if(stringRole != null){
            user.setRoles(new HashSet<>(Set.of(rolesService.getRoleByName(stringRole[0]))));
            if (stringRole.length == 2) {
                user.setRoles(new HashSet<>(Set.of(rolesService.getRoleByName(stringRole[1]))));
            }
        } else {
            user.setRoles(new HashSet<>((usersService.getUserById(id).getRoles())));
        }

        usersService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String removeUserById(@PathVariable("id") Long id) {
        usersService.removeUserById(id);
        return "redirect:/admin";
    }
}
