package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
    public Collection<Film> getAllFilms(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'\",\n",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmsMap.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, HttpServletResponse response) {
        try {
            log.info("Получен запрос к эндпоинту: /films");

            if (film.getReleaseDate().isBefore(START_DATE)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            } else if (filmsMap.containsKey(film.getId())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new FilmAlreadyExistException("Фильм с указанным названием уже был добавлен ранее");
            } else {
                film.setId(id + 1);
                filmsMap.put(film.getId(), film);
            }
        } catch (FilmAlreadyExistException | ValidationException e) {
            System.out.println(e.getMessage());
        }

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'\",\n",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            if (film.getReleaseDate().isBefore(START_DATE)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            } else if (!filmsMap.containsKey(film.getId())) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new ValidationException("ID не найден");
            } else {
                filmsMap.put(film.getId(), film);
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return film;
    }

    public void setId(int id) {
        this.id = id;
    }
}
