package ru.kata.SpringSecurityApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.SpringSecurityApp.model.Role;
import ru.kata.SpringSecurityApp.repository.RolesRepository;
import java.util.List;

@Service
public class RolesServiceImpl implements RolesService {
    private final RolesRepository roleRepository;

    @Autowired
    public RolesServiceImpl(RolesRepository rolesRepository) {
        this.roleRepository = rolesRepository;
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void removeRoleById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }


}
