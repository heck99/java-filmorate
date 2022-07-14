package ru.yandex.practicum.filmorate.storage;

import java.util.Optional;

public interface LikeReviewStorage {

    void put(long reviewId, long userId, boolean isLike);

    void delete(long reviewId, long userId);

    Optional<Boolean> getStatus(long reviewId, long userId);

    void update(long reviewId, long userId, boolean isLike);
}
