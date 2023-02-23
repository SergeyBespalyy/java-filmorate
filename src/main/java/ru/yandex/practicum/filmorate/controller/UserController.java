package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> usersMap = new HashMap<>();
    private static int id = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос к эндпоинту Get");
        return usersMap.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user, HttpServletRequest request) {

        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'\",\n",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        if (usersMap.containsKey(user.getId())) {
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
            log.info("Пользователь успешно добавлен!");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'\",\n",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        if (!usersMap.containsKey(user.getId())) {
            throw new ValidationException("ID не найден");
        }
        usersMap.put(user.getId(), user);
        return user;
    }

}
