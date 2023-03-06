package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Класс описывающий RestController следующими энпоинтами:
 * * GET "/users/{id} -  получать данные пользователя по идентификатору
 * * PUT /users/{id}/friends/{friendId} - добавление в друзья
 * * DELETE /users/{id}/friends/{friendId} - удаление из друзей
 * * GET /users/{id}/friends -  возвращаем список пользователей, являющихся его друзьями.
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage,
                          UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос к эндпоинту: Get getAllUsers");
        return inMemoryUserStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        log.info("Получен запрос к эндпоинту: Get getUserById");
        if (userId != null && userId > 0) {
            return inMemoryUserStorage.getUsersById(userId);
        }
        log.warn("Некорректный UserId");
        throw new IncorrectParameterException("userId");
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: Post addUser");
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: Put updateUser");
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer userId,
                          @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос к эндпоинту: addFriend");
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Integer userId,
                             @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос к эндпоинту: deleteFriend");
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUsersByFriend(@PathVariable("id") Integer userId) {
        log.info("Получен запрос к эндпоинту: getUsersByFriend");
        return userService.findFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getJoinFriend(@PathVariable("id") Integer userId,
                                          @PathVariable("otherId") Integer friendId) {
        log.info("Получен запрос к эндпоинту: getJoinFriend");
        return userService.findJoinFriends(userId, friendId);
    }
}
