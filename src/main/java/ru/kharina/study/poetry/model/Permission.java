package ru.kharina.study.poetry.model;

public enum Permission {
    PERMISSION_MIN("permission:min"),
    PERMISSION_USER("permission:user"),
    PERMISSION_POET("permission:poet"),
    PERMISSION_ADMIN("permission:admin");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}