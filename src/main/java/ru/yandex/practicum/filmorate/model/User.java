package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс описывающий модель User
 */
@Data
public class User {

    private Integer id;
    private Set<Integer> friendsId = new HashSet<>();

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "Поле Дата рождения не может быть пустым")
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public void setFriendsId(Integer friendId) {
        friendsId.add(friendId);
    }
}
