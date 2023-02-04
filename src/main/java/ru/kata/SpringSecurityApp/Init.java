package ru.kata.SpringSecurityApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.SpringSecurityApp.model.Role;
import ru.kata.SpringSecurityApp.model.User;
import ru.kata.SpringSecurityApp.service.RolesService;
import ru.kata.SpringSecurityApp.service.UsersService;
import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class Init {

    private final RolesService roleService;
    private final UsersService usersService;

    @Autowired
    public Init(RolesService roleService, UsersService usersService) {
        this.roleService = roleService;
        this.usersService = usersService;
    }

    @PostConstruct
    public void initialization() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);

        User admin = new User("admin", "admin", "Administrator", "admin@gmail.com",
                Set.of(roleAdmin, roleUser));
        usersService.saveUser(admin);

        roleService.saveRole(roleUser);
        User user = new User("user","user", "User", "user@gmail.com",
                Set.of(roleUser));
        usersService.saveUser(user);

    }
}
