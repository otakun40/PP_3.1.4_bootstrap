package ru.kata.SpringSecurityApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.SpringSecurityApp.model.Role;
import ru.kata.SpringSecurityApp.repository.UsersRepository;
import ru.kata.SpringSecurityApp.model.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> userById = usersRepository.findById(id);
        if (userById.isPresent()) {
            return userById.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with %s not found", id));
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return usersRepository.getUserByEmail(email);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        Optional<User> userBuId = usersRepository.findById(user.getId());
        if (userBuId.isPresent()) {
            User userFromDB = userBuId.get();
            userFromDB.setId(user.getId());
            userFromDB.setFirstName(user.getFirstName());
            userFromDB.setLastName(user.getLastName());
            userFromDB.setAge(user.getAge());
            userFromDB.setEmail(user.getEmail());
            userFromDB.setRoles(user.getRoles());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            usersRepository.save(userFromDB);
        } else {
            throw new UsernameNotFoundException(String.format("User %s with %d not found", user, user.getId()));
        }
    }

    @Transactional
    public void removeUserById(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
