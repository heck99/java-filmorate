package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

@Slf4j
@Component
@AllArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public void create(Long filmId, Long genreId) {
        String sql = "INSERT INTO film_genre" +
                "(film_id,genre_id)" +
                "VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, genreId);
        log.info("Фильму с id ={} добавлен жанру с id ={}", filmId, genreId);
    }

    @Override
    public void delete(Long filmId, Long genreId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?";
        jdbcTemplate.update(sql, filmId, genreId);
        log.info("У фильма с id = {} удалён жанр с id = {}", filmId, genreId);
    }

    @Override
    public void deleteAllFilmGenre(Long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
        log.info("У фильма с id = {} удалены все жанры", filmId);
    }


}
