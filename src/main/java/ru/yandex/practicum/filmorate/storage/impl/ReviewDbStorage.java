package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.*;

@Component
public class ReviewDbStorage implements ReviewStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review create(Review element) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"review_id"});
            stmt.setString(1, element.getContent());
            stmt.setBoolean(2, element.getIsPositive());
            stmt.setLong(3, element.getUserId());
            stmt.setLong(4, element.getFilmId());
            stmt.setInt(5, 0);
            return stmt;
        }, keyHolder);

        element.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        element.setUseful(0);
        return element;
    }

    @Override
    public Review update(Review element) {
        String sqlQuery = "UPDATE reviews SET content = ?, is_positive = ?, user_id = ?, film_id = ?, useful = ? " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery,
                element.getContent(),
                element.getIsPositive(),
                element.getUserId(),
                element.getFilmId(),
                element.getUseful(),
                element.getId());
        return element;
    }

    @Override
    public Optional<Review> getElement(Long id) {
        String sqlQuery = "SELECT * FROM reviews WHERE review_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if(rs.next()){
            return Optional.of(makeReview(rs));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Review> getAll() {
        String sql = "SELECT * FROM reviews ORDER BY useful DESC";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        return rowSetToReviewList(rs);
    }

    @Override
    public boolean delete(Long id) {
        String sqlQuery = "DELETE FROM reviews WHERE review_id = ?";
        int updatedRow =  jdbcTemplate.update(sqlQuery, id);
        return updatedRow > 0;
    }

    @Override
    public Collection<Review> findByFilmId(long id, int count) {
        String sqlQuery = "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id, count);
        return rowSetToReviewList(rs);
    }

    private Review makeReview(SqlRowSet rs) {
        long id = rs.getLong("review_id");
        String content = rs.getString("content");
        Long userId = rs.getLong("user_id");
        Long filmId = rs.getLong("film_id");
        boolean isPositive = rs.getBoolean("is_positive");
        int useful = rs.getInt("useful");
        return new Review(id, content, isPositive, userId, filmId,  useful);
    }

    private List<Review> rowSetToReviewList(SqlRowSet rs) {
        List<Review> list = new ArrayList<>();
        while (rs.next()) {
            list.add(makeReview(rs));
        }
        return list;
    }
}
