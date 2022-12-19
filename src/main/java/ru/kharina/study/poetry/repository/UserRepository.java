package ru.kharina.study.poetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kharina.study.poetry.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
