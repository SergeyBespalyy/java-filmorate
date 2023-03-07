package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс FilmService описывающий операции с фильмами,
 * — добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков.
 */
@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (filmId < 0 || userId < 0) {
            log.warn("Некорректный Id");
            throw new ValidationIdException("Некорректный ID, ID не может быть отрицательным");
        }
        Film film = filmStorage.findFilmById(filmId);
        film.addIdLikes(userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (filmId < 0 || userId < 0) {
            log.warn("Некорректный Id");
            throw new ValidationIdException("Некорректный ID, ID не может быть отрицательным");
        }
        Film film = filmStorage.findFilmById(filmId);
        film.getIdLikes().remove(userId);
        return film;
    }

    public List<Film> findPopularityFilm(Integer count) {
        return filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparing(Film::countLike).reversed())
                .limit(count).collect(Collectors.toList());
    }
}
