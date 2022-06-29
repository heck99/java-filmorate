package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorStorage extends ModelStorage<Director>{
    Collection<Director> getAllByFilmId(long filmId);
}
