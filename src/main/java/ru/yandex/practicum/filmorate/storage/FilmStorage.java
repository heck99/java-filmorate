package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage extends ModelStorage<Film>{
    Collection<Film> getPopular(int count, Optional<Integer> genreId, Optional<Integer> year);
}
