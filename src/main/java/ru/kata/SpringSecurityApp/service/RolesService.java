package ru.kata.SpringSecurityApp.service;

import ru.kata.SpringSecurityApp.model.Role;
import java.util.List;

public interface RolesService {

    List<Role> getRoles();

    void saveRole(Role roleAdmin);

    void removeRoleById(Long id);

    Role getRoleByName(String name);
}
