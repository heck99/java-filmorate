package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeReviewStorage;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeReviewDbStorage implements LikeReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void put(long reviewId, long userId, boolean isLike) {
        String sqlQuery = "INSERT INTO likes_review (review_id, user_id, is_like) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, isLike);

    }

    @Override
    public void delete(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM likes_review WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);

    }

    @Override
    public Optional<Boolean> getStatus(long reviewId, long userId) {
        String sqlQuery = "SELECT is_like FROM likes_review WHERE review_id = ? AND user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, reviewId, userId);

        if (rs.next()) {
            return Optional.of(rs.getBoolean("is_like"));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void update(long reviewId, long userId, boolean isLike) {
        String sqlQuery = "UPDATE likes_review SET is_like = ? WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, isLike, reviewId, userId);
    }
}
