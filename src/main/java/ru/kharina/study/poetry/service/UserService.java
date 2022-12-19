package ru.kharina.study.poetry.service;

import org.springframework.stereotype.Service;
import ru.kharina.study.poetry.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
