package ru.kharina.study.poetry.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Table(name="usrs")
@Entity
@Data
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Set<Role_Class> roles;
}