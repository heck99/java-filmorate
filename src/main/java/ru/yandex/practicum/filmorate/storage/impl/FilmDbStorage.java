package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getPopular(int count, Optional<Integer> genreId, Optional<Integer> year) {
        StringBuilder condition = new StringBuilder(" WHERE 1=1 ");

        if (year.isPresent()) {
            condition.append(" AND EXTRACT(YEAR FROM f.RELEASE_DATE) = ? ");
        }
        if (genreId.isPresent()) {
            condition.append(" AND fg.GENRE_ID = ? ");
        }

        String sqlQuery = "SELECT f.* " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "LEFT JOIN FILM_GENRE fg on f.film_id = fg.film_id " +
                condition.toString() +
                " GROUP BY f.film_id " +
                "ORDER BY count(l.film_id) DESC " +
                "LIMIT ?";
        SqlRowSet rs;
        if (year.isPresent() && genreId.isPresent()) {
             rs = jdbcTemplate.queryForRowSet(sqlQuery, year.get(), genreId.get(), count);
        } else if (year.isPresent()) {
            rs = jdbcTemplate.queryForRowSet(sqlQuery, year.get(), count);
        } else if (genreId.isPresent()) {
            rs = jdbcTemplate.queryForRowSet(sqlQuery,genreId.get(), count);
        } else {
            rs = jdbcTemplate.queryForRowSet(sqlQuery, count);
        }
        return rowSetToFilmList(rs);
    }

    @Override
    public Film create(Film element) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES( ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, element.getName());
            stmt.setString(2, element.getDescription());
            stmt.setDate(3, Date.valueOf(element.getReleaseDate()));
            stmt.setLong(4, element.getDuration());
            stmt.setLong(5, element.getMpa().getId());
            return stmt;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        log.info("Пользователь с id = {} создан", id);
        return getElement(id).get();
    }

    @Override
    public Film update(Film element) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? where film_id = ?";
        jdbcTemplate.update(sqlQuery, element.getName(), element.getDescription(), element.getReleaseDate(),
                element.getDuration(), element.getMpa().getId(), element.getId());
        log.info("Информация о пользователе с id = {} обновлена", element.getId());
        return getElement(element.getId()).get();
    }

    @Override
    public Optional<Film> getElement(Long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if(rs.next()) {
            log.info("Фильм с id ={} найден", id);
            return Optional.of(makeFilm(rs));
        } else {
            log.info("Фильм с id ={} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT * FROM films";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        return rowSetToFilmList(rs);
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        int rowUpdated = jdbcTemplate.update(sql, id);
        return rowUpdated > 0;
    }

    private Film makeFilm(SqlRowSet rs) {
        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = getMpaById(rs.getLong("mpa_id"));
        Film film = new Film(id, name, description, date, duration, mpa);
        return film;
    }

    private Mpa getMpaById(Long id) {
        String sql = "SELECT mpa_id, name, description FROM mpa WHERE mpa_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if(!rs.next()) {
            return null;
        }
        long mpa_id = rs.getLong("mpa_id");
        String mpa_name =  rs.getString("name");
        String mpa_description =  rs.getString("description");

        return new Mpa(mpa_id, mpa_name, mpa_description);
    }

    private List<Film> rowSetToFilmList(SqlRowSet rs) {
        List<Film> list = new ArrayList<>();
        while (rs.next()) {
            list.add(makeFilm(rs));
        }
        log.info("Найдено фильмов - {}", list.size());
        return list;
    }
}
