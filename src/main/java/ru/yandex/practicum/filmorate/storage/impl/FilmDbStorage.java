package ru.yandex.practicum.filmorate.storage.impl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Optional;

public class FilmDbStorage implements FilmStorage {
    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    @Override
    public Collection<Film> getPopular(int count) {
        return null;
    }

    @Override
    public Film create(Film element) {
        return null;
    }

    @Override
    public Film update(Film element) {
        return null;
    }

    @Override
    public Optional<Film> getElement(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Film> getAll() {
        return null;
    }
}
