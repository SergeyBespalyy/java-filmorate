package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс UserService описывающий операции с пользователями,
 * как добавление в друзья, удаление из друзей, вывод списка общих друзей.
 */
@Service
@Slf4j
public class UserService {
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (userId < 0 || friendId < 0) {
            log.warn("Некорректный Id");
            throw new UserAlreadyExistException("Некорректный ID, ID не может быть отрицательным");
        }
        User user = inMemoryUserStorage.getUsersById(userId);
        user.setFriendsId(friendId);
        User friend = inMemoryUserStorage.getUsersById(friendId);
        friend.setFriendsId(userId);
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (userId < 0 || friendId < 0) {
            log.warn("Некорректный Id");
            throw new UserAlreadyExistException("Некорректный ID, ID не может быть отрицательным");
        }
        User user = inMemoryUserStorage.getUsersById(userId);
        user.getFriendsId().remove(friendId);
        return user;
    }

    public List<User> findJoinFriends(Integer userId, Integer friendId) {
        if (userId < 0 || friendId < 0) {
            log.warn("Некорректный Id");
            throw new UserAlreadyExistException("Некорректный ID, ID не может быть отрицательным");
        }
        User user = inMemoryUserStorage.getUsersById(userId);
        Set<Integer> listFriendUser = user.getFriendsId();
        User friend = inMemoryUserStorage.getUsersById(friendId);
        Set<Integer> listFriendToFriend = friend.getFriendsId();

        Set<Integer> resultList = new HashSet<>();
        resultList.addAll(listFriendToFriend);
        resultList.addAll(listFriendUser);
        return resultList.stream()
                .map(a -> inMemoryUserStorage.getUsersById(a))
                .filter(a -> !a.getId().equals(userId) && !a.getId().equals(friendId))
                .collect(Collectors.toList());
    }

    public List<User> findFriends(Integer userId) {
        if (userId < 0) {
            log.warn("Некорректный Id");
            throw new UserAlreadyExistException("Некорректный ID, ID не может быть отрицательным");
        }
        User user = inMemoryUserStorage.getUsersById(userId);
        Set<Integer> listFriendUser = user.getFriendsId();
        return listFriendUser.stream()
                .map(a -> inMemoryUserStorage.getUsersById(a))
                .collect(Collectors.toList());
    }
}
