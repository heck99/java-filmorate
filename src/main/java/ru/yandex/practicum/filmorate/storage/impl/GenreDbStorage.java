package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Component
public class GenreDbStorage implements GenreStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAll() {
        String sql = "SELECT * FROM genres";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        return rowSetToGenreList(rs);
    }

    @Override
    public Collection<Genre> getAllByFilmId(Long id) {
        String sql = "SELECT g.* FROM genres g LEFT JOIN film_genre f_g ON g.genre_id = f_g.genre_id" +
                " WHERE f_g.film_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        return rowSetToGenreList(rs);
    }

    @Override
    public Genre create(Genre element) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres").usingGeneratedKeyColumns("genre_id");
        long genreId = simpleJdbcInsert.executeAndReturnKey(genreToMap(element)).longValue();
        log.info(String.format("Жанр с id = %d создан", genreId));
        element.setId(genreId);
        return element;
    }

    @Override
    public Genre update(Genre element) {
        String sqlQuery = "UPDATE genres SET name = ? where genre_id = ?";
        jdbcTemplate.update(sqlQuery, element.getName(), element.getId());
        log.info("Информация о жанре с id = {} обновлена", element.getId());
        return getElement(element.getId()).get();
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM genres WHERE genre_id = ?";
        int rowUpdated = jdbcTemplate.update(sql, id);
        return rowUpdated > 0;
    }

    @Override
    public Optional<Genre> getElement(Long id) {
        String sql = "SELECT * FROM genres where genre_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if(rs.next()) {
            log.info("Жанр с id = {} найден", id);
            return Optional.of(makeGenre(rs));
        } else {
            log.info("Жанр с id = {} не найден", id);
            return Optional.empty();
        }
    }

    private Genre makeGenre(SqlRowSet rs){
        Long id = rs.getLong("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    private Collection<Genre> rowSetToGenreList(SqlRowSet rs) {
        Collection<Genre> list = new ArrayList<>();
        while (rs.next()) {
            list.add(makeGenre(rs));
        }
        log.info("Возвращено {} жанров",list.size());
        return list;
    }

    private Map<String, Object> genreToMap(Genre genre) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", genre.getName());
        return values;
    }

}
