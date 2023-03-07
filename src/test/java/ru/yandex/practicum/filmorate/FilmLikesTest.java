package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmLikesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private FilmStorage filmStorage;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"nisieiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\",\"duration\":100}"));

    }


    @Test
    public void shouldGetFilmPost() throws Exception {
        mockMvc.perform(get("/films/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("nisieiusmod"))
                .andExpect(jsonPath("$.description").value("adipisicing"))
                .andExpect(jsonPath("$.releaseDate").value("1967-03-25"))
                .andExpect(jsonPath("$.duration").value("100"));
    }

    @Test
    public void shouldGetPopularFilm() throws Exception {
        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk());

        assertEquals(1, filmService.findPopularityFilm(2).size());
    }

    @Test
    public void shouldAddAndGetFilmWhenAddTwoFilms() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"));

        mockMvc.perform(get("/films/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("New film"))
                .andExpect(jsonPath("$.description").value("New film about friends"))
                .andExpect(jsonPath("$.releaseDate").value("1999-04-30"))
                .andExpect(jsonPath("$.duration").value("120"));

        assertEquals(2, filmStorage.getAllFilm().size());
    }

    @Test
    public void shouldGetFilmWhenUnknownId() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"));

        mockMvc.perform(get("/films/{id}", 999))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddLikeFilm() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        Integer idUser = 1;
        Integer idFilm = 2;

        assertEquals(0, filmStorage.findFilmById(2).getIdLikes().size());

        mockMvc.perform(put("/films/{id}/like/{userId}", idFilm, idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"))
                .andExpect(status().isOk());

        assertEquals(1, filmStorage.findFilmById(2).getIdLikes().size());
    }

    @Test
    public void shouldDeleteLikeFilm() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        Integer idUser = 1;
        Integer idFilm = 2;

        mockMvc.perform(put("/films/{id}/like/{userId}", idFilm, idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"))
                .andExpect(status().isOk());

        assertEquals(1, filmStorage.findFilmById(idFilm).getIdLikes().size());

        mockMvc.perform(delete("/films/{id}/like/{userId}", idFilm, idUser))
                .andExpect(status().isOk());

        assertEquals(0, filmStorage.findFilmById(idFilm).getIdLikes().size());
    }

    @Test
    public void shouldDeleteLikeFilmWhenUserIdUnknown() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        Integer idUser = 1;
        Integer idFilm = -2;

        mockMvc.perform(put("/films/{id}/like/{userId}", idFilm, idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New film\",\"releaseDate\":\"1999-04-30\",\"description\":\"New film about friends\",\"duration\":120,\"rate\":4}"))
                .andExpect(status().is4xxClientError());
    }
}
