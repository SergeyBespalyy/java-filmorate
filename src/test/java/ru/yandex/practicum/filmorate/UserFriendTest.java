package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserFriendTest {
    @Autowired()
    private MockMvc mockMvc;

    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() throws Exception {

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}"));
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"common\",\"name\":\"\",\"email\":\"friend@common.ru\",\"birthday\":\"2000-08-20\"}"));
    }

    @Test
    public void shouldAddFriend() throws Exception {

        System.out.println(userStorage.getAllUsers());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        Integer idUser3 = 3;

        System.out.println(userStorage.getAllUsers());

        mockMvc.perform(get("/users/{id}", idUser3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("friend@mail.ru"))
                .andExpect(jsonPath("$.name").value("friend adipisicing"))
                .andExpect(jsonPath("$.login").value("friend"))
                .andExpect(jsonPath("$.birthday").value("1976-08-20"));
    }

    @Test
    public void shouldAddTwoFriendForUSer() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        Integer idUser1 = 1;
        Integer idUser2 = 2;

        mockMvc.perform(get("/users/{id}", idUser1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));

        mockMvc.perform(get("/users/{id}", idUser2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("friend@common.ru"))
                .andExpect(jsonPath("$.name").value("common"))
                .andExpect(jsonPath("$.login").value("common"))
                .andExpect(jsonPath("$.birthday").value("2000-08-20"));
    }

    @Test
    public void shouldGetUserByUnknownId() throws Exception {
        Integer idUser = 9999;
        mockMvc.perform(get("/users/{id}", idUser))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetCommonFriendWhenEmpty() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = 2;
        User user1 = userStorage.getUsersById(idUser1);
        User user2 = userStorage.getUsersById(idUser2);

        assertEquals(0, user1.getFriendsId().size());
        assertEquals(0, user2.getFriendsId().size());

        mockMvc.perform(get("/users/{id}/friends/common/{otherId}", idUser1, idUser2))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldPutFriend() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = 2;
        User user1 = userStorage.getUsersById(idUser1);
        User user2 = userStorage.getUsersById(idUser2);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser2))
                .andExpect(status().isOk());

        assertEquals(1, user1.getFriendsId().size());
        assertEquals(1, user2.getFriendsId().size());
    }

    @Test
    public void shouldPutFriendWhenIdUnknown() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = -2;
        User user1 = userStorage.getUsersById(1);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser2))
                .andExpect(status().is4xxClientError());

        assertEquals(0, user1.getFriendsId().size());
    }

    @Test
    public void shouldGetFriendById() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = 2;
        User user1 = userStorage.getUsersById(idUser1);
        User user2 = userStorage.getUsersById(idUser2);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser2));

        mockMvc.perform(get("/users/{id}/friends", idUser1)).andExpect(status().isOk());
        mockMvc.perform(get("/users/{id}/friends", idUser2)).andExpect(status().isOk());

        assertEquals(List.of(user2), userService.findFriends(1));
        assertEquals(List.of(user1), userService.findFriends(2));
    }

    @Test
    public void shouldGetCommonFriendWhenPut() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = 2;
        User user1 = userStorage.getUsersById(idUser1);
        User user2 = userStorage.getUsersById(idUser2);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser2))
                .andExpect(status().isOk());

        assertEquals(0, userService.findJoinFriends(idUser1, idUser2).size());
    }

    @Test
    public void shouldPutFriendThree() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = 2;
        Integer idUser3 = 3;

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        User user1 = userStorage.getUsersById(idUser1);
        User user2 = userStorage.getUsersById(idUser2);
        User user3 = userStorage.getUsersById(idUser3);


        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser2))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser3))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser2, idUser3))
                .andExpect(status().isOk());

        assertEquals(List.of(user2, user3), userService.findFriends(1));
        assertEquals(List.of(user1, user3), userService.findFriends(2));
        assertEquals(List.of(user1, user2), userService.findFriends(3));

    }

    @Test
    public void shouldGetCommonFriend() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = 2;
        Integer idUser3 = 3;

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        User user1 = userStorage.getUsersById(idUser1);
        User user2 = userStorage.getUsersById(idUser2);
        User user3 = userStorage.getUsersById(idUser3);


        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser2))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser3))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser2, idUser3))
                .andExpect(status().isOk());

        assertEquals(List.of(user3), userService.findJoinFriends(idUser1, idUser2));
    }

    @Test
    public void shouldDeleteFriend() throws Exception {
        Integer idUser1 = 1;
        Integer idUser2 = 2;
        Integer idUser3 = 3;

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(" {\"login\":\"friend\",\"name\":\"friend adipisicing\",\"email\":\"friend@mail.ru\",\"birthday\":\"1976-08-20\"}"));

        User user1 = userStorage.getUsersById(idUser1);
        User user2 = userStorage.getUsersById(idUser2);
        User user3 = userStorage.getUsersById(idUser3);


        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser2))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser1, idUser3))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/{id}/friends/{friendId}", idUser2, idUser3))
                .andExpect(status().isOk());

        assertEquals(List.of(user2, user3), userService.findFriends(idUser1));

        mockMvc.perform(delete("/users/{id}/friends/{friendId}", idUser1, idUser2))
                .andExpect(status().isOk());

        assertEquals(List.of(user3), userService.findFriends(idUser1));
    }
}