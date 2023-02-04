package ru.kata.SpringSecurityApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.SpringSecurityApp.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
}
