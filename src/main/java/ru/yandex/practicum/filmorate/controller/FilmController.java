package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Класс описывающий RestController следующими энпоинтами:
 * GET "/films/{id} -  получать фильм по их идентификатору
 * PUT /films/{id}/like/{userId} - пользователь ставит лайк фильму.
 * DELETE /films/{id}/like/{userId} - пользователь удаляет лайк.
 * GET /films/popular?count={count} - возвращает список из первых count фильмов по количеству лайков.
 */

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private InMemoryFilmStorage inMemoryFilmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage,
                          FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос к эндпоинту: getAllFilms");
        return inMemoryFilmStorage.findAllFilm();
    }

    @GetMapping("/{id}")
    public Film getFilmsById(@PathVariable(required = false, name = "id") Integer id) {
        log.info("Получен запрос к эндпоинту: getFilmsById");
        if (id != null) {
            return inMemoryFilmStorage.findFilmById(id);
        }
        log.warn("Некорректный FilmId");
        throw new IncorrectParameterException("FilmId");
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: addFilm");
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: updateFilm");
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikeToFilm(@PathVariable("id") Integer filmId,
                              @PathVariable("userId") Integer userId) {
        log.info("Получен запрос к эндпоинту: addLikeToFilm");
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeToFilm(@PathVariable("id") Integer filmId,
                                 @PathVariable("userId") Integer userId) {
        log.info("Получен запрос к эндпоинту: deleteLikeToFilm");
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getFilmsByCount(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос к эндпоинту: getFilmsByCount");
        return filmService.findPopularityFilm(count);
    }
}
