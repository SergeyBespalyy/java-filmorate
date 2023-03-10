package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс описывающий модель Film
 */
@Data
public class Film {

    private int id;
    private Set<Integer> idLikes = new HashSet<>();

    @NotEmpty(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;


    @NotNull(message = "Дата релиза не может быть пустым")
    private LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private int duration;

    public void addIdLikes(Integer idFriend) {
        idLikes.add(idFriend);
    }

    public Integer countLike() {
        return idLikes.size();
    }
}
