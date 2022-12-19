package ru.kharina.study.poetry.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kharina.study.poetry.model.Role_Class;
import ru.kharina.study.poetry.model.Status;
import ru.kharina.study.poetry.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUsrDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private User user;

    public CustomUsrDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role_Class> roles = user.getRoles();
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for(Role_Class role : roles) {
            authorities.addAll(role.getRoleName().getAuthorities());
        }
        return authorities;
    }

    @Override
    public String getPassword() {return user.getPassword();}

    @Override
    public String getUsername() {return user.getEmail();}

    @Override
    public boolean isAccountNonExpired() {return user.getStatus().equals(Status.ACTIVE);}

    @Override
    public boolean isAccountNonLocked() {return user.getStatus().equals(Status.ACTIVE);}

    @Override
    public boolean isCredentialsNonExpired() {return user.getStatus().equals(Status.ACTIVE);}

    @Override
    public boolean isEnabled() {return user.getStatus().equals(Status.ACTIVE);}
}