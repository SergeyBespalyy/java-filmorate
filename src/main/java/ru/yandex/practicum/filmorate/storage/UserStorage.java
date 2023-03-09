package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


/**
 * Интерфейс UserStorage имеющий
 * методы добавления, удаления и модификации объектов
 */
public interface UserStorage {

    Collection<User> getAllUsers();

    User getUsersById(Integer id);

    User addUser(User user);

    void deleteUser(User user);

    User updateUser(User user);
}
