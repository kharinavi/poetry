package ru.kharina.study.poetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kharina.study.poetry.model.Role_Class;

@Repository
public interface RoleRepository extends JpaRepository<Role_Class, Long> {
}
