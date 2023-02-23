package ru.yandex.practicum.filmorate.model;

import com.sun.source.tree.BreakTree;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    private int id;

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "Поле Дата рождения не может быть пустым")
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

}
