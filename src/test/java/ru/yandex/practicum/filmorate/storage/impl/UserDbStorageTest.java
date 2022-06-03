package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.getElement(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetAll() {
        Collection<User> users = userStorage.getAll();
        assertThat(users).hasSize(4);
    }

    @Test
    public void testGetFriends() {
        Collection<User> users = userStorage.getFriends(1L);
        assertThat(users).hasSize(2);
    }

    @Test
    public void testGetCommonFriends() {
        Collection<User> users = userStorage.getCommonFriends(1L, 3L);
        assertThat(users).hasSize(1).flatMap(User::getId).isSubsetOf(2L);
    }

    @Test
    public void testCreateUser() {
        User user = new User("newName", "newemail@mail.ru", "newLogin", LocalDate.of(200, 10, 10));
        Collection<User> users = userStorage.getAll();
        assertThat(users).hasSize(4);
        userStorage.create(user);
        users = userStorage.getAll();
        assertThat(users).hasSize(5);
        Optional<User> userOptional = userStorage.getElement(5L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("name", "newName")
                );
    }

    @Test
    public void testUpdateElement() {
        Optional<User> userOptional = userStorage.getElement(2L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("name", "name2")
                );
        User user = new User(2L, "newName", "newemail@mail.ru", "newLogin", LocalDate.of(200, 10, 10));
        userStorage.update(user);
        userOptional = userStorage.getElement(2L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("name", "newName")
                );
    }
}