package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LikeDbStorageTest {

    private final LikeDbStorage likeStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testAddLike() {
        Collection<Film> populars = filmStorage.getPopular(3);
        assertThat(populars).element(0).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(populars).element(1).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(populars).element(2).hasFieldOrPropertyWithValue("id", 1L);
        likeStorage.addLike(1L, 2L);
        likeStorage.addLike(1L, 3L);
        likeStorage.addLike(1L, 4L);
        populars = filmStorage.getPopular(3);
        assertThat(populars).element(0).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(populars).element(1).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(populars).element(2).hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    public void testDeleteLike() {
        Collection<Film> populars = filmStorage.getPopular(3);
        assertThat(populars).element(0).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(populars).element(1).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(populars).element(2).hasFieldOrPropertyWithValue("id", 1L);
        likeStorage.deleteLike(3L, 1L);
        likeStorage.deleteLike(3L, 2L);
        likeStorage.deleteLike(3L, 3L);
        populars = filmStorage.getPopular(3);
        assertThat(populars).element(0).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(populars).element(1).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(populars).element(2).hasFieldOrPropertyWithValue("id", 3L);
    }

}