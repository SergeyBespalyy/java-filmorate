package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс описывающий RestController "/users"
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> usersMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос к эндпоинту: Get getAllUsers");
        return usersMap.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: Post addUser");
        if (usersMap.containsKey(user.getId())) {
            log.warn("ОШИБКА: Пользователь с данной электронной почтой уже зарегистрирован.");
            throw new UserAlreadyExistException("Пользователь с данной электронной почтой уже зарегистрирован.");
        } else {
            if (user.getId() == 0) {
                user.setId(++id);
            }
            String userName = user.getName();
            if (userName == null) {
                user.setName(user.getLogin());
            }
            usersMap.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: Put updateUser");
        if (!usersMap.containsKey(user.getId())) {
            log.warn("ОШИБКА: ID не найден");
            throw new UserAlreadyExistException("ID не найден");
        } else usersMap.put(user.getId(), user);
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }
}
