package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс InMemoryFilmStorage описывает логику хранения, обновления и поиска объектов.
 */
@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    public final LocalDate START_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> filmsMap = new HashMap<>();
    private static Integer filmId = 1;


    @Override
    public Collection<Film> findAllFilm() {
        return filmsMap.values();
    }

    @Override
    public Film findFilmById(Integer filmId) {
        if (filmsMap.containsKey(filmId)) {
            return filmsMap.get(filmId);
        }
        throw new FilmAlreadyExistException("ID film не найден");
    }

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(START_DATE)) {
            log.warn("ОШИБКА: Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (filmsMap.containsKey(film.getId())) {
            log.warn("ОШИБКА: Фильм с указанным названием уже был добавлен ранее");
            throw new FilmAlreadyExistException("Фильм с указанным названием уже был добавлен ранее");
        } else {
            film.setId(getNextId());
            filmsMap.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (!filmsMap.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("ID не найден");
        }
        filmsMap.remove(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
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

    private static Integer getNextId() {
        return filmId++;
    }
}
