package ru.kharina.study.poetry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kharina.study.poetry.model.Poet;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoemDto {
    private Long id;
    private String name;
    private String text;
    private PoetDto author;
}
