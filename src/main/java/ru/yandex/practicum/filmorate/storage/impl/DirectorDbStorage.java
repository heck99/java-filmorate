package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Component
public class DirectorDbStorage implements DirectorStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public Director create(Director element) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors").usingGeneratedKeyColumns("director_id");
        long directorId = simpleJdbcInsert.executeAndReturnKey(directorToMap(element)).longValue();
        log.info(String.format("Режиссёр с id = %d создан", directorId));
        element.setId(directorId);
        return element;
    }

    @Override
    public Director update(Director element) {
        String sqlQuery = "UPDATE directors SET name = ? where director_id = ?";
        jdbcTemplate.update(sqlQuery, element.getName(), element.getId());
        log.info("Информация о режиссёре с id = {} обновлена", element.getId());
        return getElement(element.getId()).get();
    }

    @Override
    public Optional<Director> getElement(Long id) {
        String sql = "SELECT * FROM directors where director_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if(rs.next()) {
            log.info("Режиссёр с id = {} найден", id);
            return Optional.of(makeDirector(rs));
        } else {
            log.info("Режиссёр с id = {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Director> getAll() {
        String sql = "SELECT * FROM directors";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        return rowSetToDirectorList(rs);
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM directors WHERE director_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public Collection<Director> getAllByFilmId(long filmId) {
        String sql = "SELECT d.* FROM directors d LEFT JOIN film_director f_d ON d.director_id = f_d.director_id" +
                " WHERE f_d.film_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, filmId);
        return rowSetToDirectorList(rs);
    }

    private Director makeDirector(SqlRowSet rs){
        Long id = rs.getLong("director_id");
        String name = rs.getString("name");
        return new Director(id, name);
    }

    private Collection<Director> rowSetToDirectorList(SqlRowSet rs) {
        Collection<Director> list = new ArrayList<>();
        while (rs.next()) {
            list.add(makeDirector(rs));
        }
        log.info("Возвращено {} режиссёров",list.size());
        return list;
    }

    private Map<String, Object> directorToMap(Director director) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());
        return values;
    }


}
