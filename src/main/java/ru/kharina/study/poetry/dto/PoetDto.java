package ru.kharina.study.poetry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kharina.study.poetry.model.Role_Class;
import ru.kharina.study.poetry.model.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoetDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private int securityId;
    private List<PoemDto> poems;
    private List<PoemDto> favorites;
}
