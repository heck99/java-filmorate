package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FriendDbStorageTest {

    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;

    @Test
    public void testDeleteFriend() {
        Collection<User> users = userStorage.getFriends(1L);
        assertThat(users).hasSize(2);
        friendStorage.deleteFriend(1L,2L);
        users = userStorage.getFriends(1L);
        assertThat(users).hasSize(1);
    }

    @Test
    public void testAddFriend() {
        Collection<User> users = userStorage.getFriends(1L);
        assertThat(users).hasSize(2);
        friendStorage.addFriend(1L, 3L);
        users = userStorage.getFriends(1L);
        assertThat(users).hasSize(3);
    }

}