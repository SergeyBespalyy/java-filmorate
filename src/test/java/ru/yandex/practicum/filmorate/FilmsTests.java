package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Класс описывающий тесты к контроллеру FilmController "/films"
 */

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmsTests {
    @Autowired
    private MockMvc mockMvc;

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
    public void shouldAdFilmPostWhenFailName() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":\"Description\",\"releaseDate\":\"1900-03-25\",\"duration\":200}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddFilmPostWhenFailDescription() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Film name\",\"description\":\"Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.\",\"releaseDate\":\"1967-03-25\",\"duration\":200}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddFilmPostWhenFailReleaseDate() throws Exception {
        mockMvc.perform(post("/films").
                        contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"nisieiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1890-03-25\",\"duration\":100}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddFilmPostWhenFailDuration() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Film name\",\"description\":\"Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль.\",\"releaseDate\":\"1967-03-25\",\"duration\":-200}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldUpdatePutFilmWhenStatus200() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"name\":\"FilmUpdated\",\"releaseDate\":\"1989-04-17\",\"description\":\"Newfilmupdatedecription\",\"duration\":190,\"rate\":4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("FilmUpdated"))
                .andExpect(jsonPath("$.releaseDate").value("1989-04-17"))
                .andExpect(jsonPath("$.description").value("Newfilmupdatedecription"))
                .andExpect(jsonPath("$.duration").value("190"));
    }

    @Test
    public void shouldUpdatePutWhenIdUnknown() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"id\":9999,\"name\":\"FilmUpdated\",\"releaseDate\":\"1989-04-17\",\"description\":\"Newfilmupdatedecription\",\"duration\":190,\"rate\":4}"))
                .andExpect(status().is4xxClientError());
    }
}