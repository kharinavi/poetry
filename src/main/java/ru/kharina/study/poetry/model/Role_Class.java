package ru.kharina.study.poetry.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="roles")
@Entity
@Data
public class Role_Class {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role")
    @Enumerated(value = EnumType.STRING)
    private Role roleName;
}
