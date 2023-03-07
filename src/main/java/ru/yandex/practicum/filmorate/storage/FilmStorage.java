package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Интерфейс FilmStorage имеющий
 * методы добавления, удаления и модификации объектов
 */

public interface FilmStorage {

    Collection<Film> getAllFilm();

    Film findFilmById(Integer filmId);

    Film addFilm(Film film);

    void deleteFilm(Film film);

    Film updateFilm(Film film);

}
