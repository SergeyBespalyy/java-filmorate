package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.AfterEach;
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
 * Класс описывающий тесты к контроллеру UserController "/users"
 */

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsersTests {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"));
    }

    @Test
    public void shouldGetUserById() throws Exception {
        Integer idUser = 1;
        mockMvc.perform(get("/users/{id}", idUser))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    public void shouldAdUserPostWhenFailLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore ullamco\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenFailEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenFailBirthday() throws Exception {
        mockMvc.perform(post("/users").
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
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"doloreUpdate\",\"name\":\"estadipisicing\",\"id\":9999,\"email\":\"mail@yandex.ru\",\"birthday\":\"1976-09-20\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenEmptyName() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\",\"email\":\"mail2@mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("dolore"));
    }
}
