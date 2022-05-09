package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends ModelStorage<Film>{
    void addLike (Long filmId, Long userId);
    void deleteLike (Long filmId, Long userId);
    Collection<Film> getPopular(int count);
}
