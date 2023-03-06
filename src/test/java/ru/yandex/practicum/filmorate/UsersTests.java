package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Класс описывающий тесты к контроллеру UserController "/users"
 */

@SpringBootTest
@AutoConfigureMockMvc
public class UsersTests {
    @Autowired
    private MockMvc mockMvc;
    private ResultActions resultActions;

    @BeforeEach
    public void setUp() throws Exception {
        resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"));

    }

    @Test
    public void shouldAddUserPost() throws Exception {
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    public void shouldAdUserPostWhenFailLogin() throws Exception {
        resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore ullamco\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenFailEmail() throws Exception {
        resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenFailBirthday() throws Exception {
        resultActions = mockMvc.perform(post("/users").
                        contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"2446-08-20\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldUpdatePutUserWhenStatus200() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"doloreUpdate\",\"name\":\"estadipisicing\",\"id\":1,\"email\":\"mail@yandex.ru\",\"birthday\":\"1976-09-20\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mail@yandex.ru"))
                .andExpect(jsonPath("$.name").value("estadipisicing"))
                .andExpect(jsonPath("$.login").value("doloreUpdate"))
                .andExpect(jsonPath("$.birthday").value("1976-09-20"));
    }

    @Test
    public void shouldUpdatePutWhenIdUnknown() throws Exception {
        resultActions = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"doloreUpdate\",\"name\":\"estadipisicing\",\"id\":9999,\"email\":\"mail@yandex.ru\",\"birthday\":\"1976-09-20\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenEmptyName() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.name").value("dolore"));
    }
}
