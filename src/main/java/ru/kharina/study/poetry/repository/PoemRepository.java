package ru.kharina.study.poetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kharina.study.poetry.model.Poem;

@Repository
public interface PoemRepository extends JpaRepository<Poem, Long> {
}
