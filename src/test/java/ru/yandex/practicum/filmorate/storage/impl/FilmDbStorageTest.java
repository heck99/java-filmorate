package ru.yandex.practicum.filmorate.storage.impl;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {

     private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() {
        Optional<Film> filmOptional = filmStorage.getElement(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetAll() {
        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(3);
    }

    @Test
    public void testCreateFilm() {
        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(3);
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        Film film = new Film("newName", "newDescription", LocalDate.now(), 133, mpa);
        filmStorage.create(film);
        films = filmStorage.getAll();
        assertThat(films).hasSize(4);
        Optional<Film> filmOptional = filmStorage.getElement(4L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "newName")
                );
    }

    @Test
    public void testUpdateFilm() {
        Optional<Film> filmOptional = filmStorage.getElement(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "name1")
                );
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        Film film = new Film(1L,"newName", "newDescription", LocalDate.now(), 133, mpa);
        filmStorage.update(film);
        filmOptional = filmStorage.getElement(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "newName")
                );
    }

    @Test
    public void testGetPopular() {
        Collection<Film> populars = filmStorage.getPopular(2, Optional.empty(), Optional.empty());
        assertThat(populars).hasSize(2);
        assertThat(populars).element(0).hasFieldOrPropertyWithValue("id", 3L);
    }

}
