package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    public final LocalDate START_DATE = LocalDate.of(1895, 12, 28 );
    private final Map<Integer,Film> filmsMap = new HashMap<>();
    private static int id = 0;

    @GetMapping
    public Collection<Film> getAllFilms(HttpServletRequest request){
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'\",\n",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmsMap.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, HttpServletRequest request){

        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'\",\n",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        if (film.getReleaseDate().isBefore(START_DATE)){
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (filmsMap.containsKey(film.getId())){
            throw new FilmAlreadyExistException("Фильм с указанным названием уже был добавлен ранее");
        } else {
            film.setId(id+1);
            filmsMap.put(film.getId(), film);
            log.info("Фильм успешно добавлен!");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, HttpServletRequest request){
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'\",\n",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        if (film.getReleaseDate().isBefore(START_DATE)){
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (!filmsMap.containsKey(film.getId())) {
            throw new ValidationException("ID не найден");
        }
        filmsMap.put(film.getId(), film);
        return film;
    }
}
