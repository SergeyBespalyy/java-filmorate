package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс описывающий RestController "/films"
 */

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    public final LocalDate START_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> filmsMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос к эндпоинту: Get getAllFilms");
        return filmsMap.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        log.info("Получен запрос к эндпоинту: Post addFilm");

        if (film.getReleaseDate().isBefore(START_DATE)) {
            log.warn("ОШИБКА: Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (filmsMap.containsKey(film.getId())) {
            log.warn("ОШИБКА: Фильм с указанным названием уже был добавлен ранее");
            throw new FilmAlreadyExistException("Фильм с указанным названием уже был добавлен ранее");
        } else {
            film.setId(id + 1);
            filmsMap.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: Put updateFilm");
        if (film.getReleaseDate().isBefore(START_DATE)) {
            log.warn("ОШИБКА: Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (!filmsMap.containsKey(film.getId())) {
            log.warn("ОШИБКА: ID не найден");
            throw new FilmAlreadyExistException("ID не найден");
        } else {
            filmsMap.put(film.getId(), film);
        }
        return film;
    }

    public void setId(int id) {
        this.id = id;
    }
}
