package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController controller;
    private ResultActions resultActions;


    @Test
    public void shouldAddUserPost() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"))
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
        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"dolore ullamco\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenFailEmail() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenFailBirthday() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/users").
                contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"2446-08-20\"}"))
                .andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddUserPostWhenEmptyName() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\",\"email\":\"mail.ru\",\"birthday\":\"1946-08-20\"}"))
                .andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void shouldUpdatePutUserWhenStatus200() throws Exception {

        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content("{\"login1\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"1mail@mail.ru\",\"birthday\":\"2446-08-20\"}"))
                .andDo(print()).andExpect(status().is4xxClientError());
    }
}
