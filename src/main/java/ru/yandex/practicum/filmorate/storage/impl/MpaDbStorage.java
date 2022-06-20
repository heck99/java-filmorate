package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {

    JdbcTemplate jdbcTemplate;

    public Optional<Mpa> getElement(Long id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            log.info("Mpa с id = {} найден", id);
            return Optional.of(makeMpa(rs));
        } else {
            log.info("Mpa с id = {} не найден", id);
            return Optional.empty();
        }
    }

    public Collection<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        return rowSetToMpaList(rs);
    }

    private Mpa makeMpa(SqlRowSet rs) {
        Long id = rs.getLong("mpa_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        return new Mpa(id, name, description);
    }

    private Collection<Mpa> rowSetToMpaList(SqlRowSet rs) {
        Collection<Mpa> col = new ArrayList<>();
        while (rs.next()) {
            col.add(makeMpa(rs));
        }
        log.info("Возвращено {} пользователей", col.size());
        return col;
    }
}
