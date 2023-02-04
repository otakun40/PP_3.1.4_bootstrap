package ru.kata.SpringSecurityApp.service;

import ru.kata.SpringSecurityApp.model.User;
import java.util.List;

public interface UsersService {
    void saveUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    void updateUser(User user);


    void removeUserById(Long id);
}
