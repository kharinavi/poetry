package ru.kharina.study.poetry.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.PERMISSION_MIN, Permission.PERMISSION_USER)),
    POET(Set.of(Permission.PERMISSION_MIN, Permission.PERMISSION_USER, Permission.PERMISSION_POET)),
    ADMIN(Set.of(Permission.PERMISSION_MIN, Permission.PERMISSION_POET, Permission.PERMISSION_ADMIN));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}