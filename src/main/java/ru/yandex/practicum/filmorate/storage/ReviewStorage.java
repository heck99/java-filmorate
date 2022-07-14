package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewStorage extends ModelStorage<Review> {
    Collection<Review> findByFilmId(long id, int count);

}
