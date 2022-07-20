package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmDirectorStorage;

@Slf4j
@Component
@AllArgsConstructor
public class FilmDirectorDbStorage implements FilmDirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(Long filmId, Long directorId) {
        String sql = "INSERT INTO film_director" +
                "(film_id,director_id)" +
                "VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, directorId);
        log.info("Фильму с id ={} добавлен режиссёр с id ={}", filmId, directorId);
    }

    @Override
    public void delete(Long filmId, Long directorId) {
        String sql = "DELETE FROM film_director WHERE film_id = ? AND director_id = ?";
        jdbcTemplate.update(sql, filmId, directorId);
        log.info("У фильма с id = {} удалён режиссёр с id = {}", filmId, directorId);
    }

    @Override
    public void deleteAllFilmDirectors(Long filmId) {
        String sql = "DELETE FROM film_director WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
        log.info("У фильма с id = {} удалены все режиссёры", filmId);
    }
}
