package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;


public interface GenreStorage extends ModelStorage<Genre> {
    public Collection<Genre> getAllByFilmId(Long id);
}
