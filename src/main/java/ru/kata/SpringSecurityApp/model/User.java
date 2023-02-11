package ru.kata.SpringSecurityApp.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Firstname should not be empty")
    @Column(name = "firstname")
    private String firstName;

    @NotEmpty(message = "Lastname should not be empty")
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "age")
    @Min(value = 0, message = "age should not be negative")
    @Max(value = 150, message = "age should be less than 150")
    private int age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Enter correct Email address")
    @Column(name = "email")
    private String email;


    @NotEmpty(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

@ManyToMany
@JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
)
    private Set<Role> roles;

    public User(String firstName, String lastName, int age, String email, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
                && age == user.age && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, password, id, age, email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
