package ru.kharina.study.poetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kharina.study.poetry.model.Poet;

@Repository
public interface PoetRepository extends JpaRepository<Poet, Long> {
}
