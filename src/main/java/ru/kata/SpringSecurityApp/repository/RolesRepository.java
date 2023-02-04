package ru.kata.SpringSecurityApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.SpringSecurityApp.model.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
    Role getRoleByName(String name);
}
