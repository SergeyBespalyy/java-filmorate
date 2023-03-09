package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс InMemoryUserStorage описывает логику хранения, обновления и поиска объектов.
 */

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> usersMap = new HashMap<>();
    private Integer userId = 1;

    @Override
    public Collection<User> getAllUsers() {
        return usersMap.values();
    }

    public User getUsersById(Integer id) {
        if (usersMap.containsKey(id)) {
            return usersMap.get(id);
        }
        throw new UserAlreadyExistException("Пользователь не найден");
    }

    @Override
    public User addUser(User user) {
        if (usersMap.containsKey(user.getId())) {
            log.warn("ОШИБКА: Пользователь с данным ID уже зарегистрирован.");
            throw new UserAlreadyExistException("Пользователь с данным ID уже зарегистрирован.");
        } else {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(getNextId());

            boolean userEmail = usersMap.values().stream()
                    .anyMatch(f -> f.getEmail().equals(user.getEmail()));
            if (userEmail) {
                log.warn("ОШИБКА: Пользователь с данной электронной почтой уже зарегистрирован.");
                throw new UserAlreadyExistException("Пользователь с данной электронной почтой уже зарегистрирован.");
            }
            usersMap.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);
            return user;
        }
        log.warn("ОШИБКА: ID не найден");
        throw new UserAlreadyExistException("ID не найден");
    }

    @Override
    public void deleteUser(User user) {
        if (!usersMap.containsKey(user.getId())) {
            log.warn("ОШИБКА: ID не найден");
            throw new UserAlreadyExistException("ID не найден");
        }
        usersMap.remove(user.getId());
    }

    private Integer getNextId() {
        return userId++;
    }
}
