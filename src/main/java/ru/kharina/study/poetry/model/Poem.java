package ru.kharina.study.poetry.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "poems")
public class Poem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "author")
    private Poet author;
}
