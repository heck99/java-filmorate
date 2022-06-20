package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

public interface MpaStorage {
    public Optional<Mpa> getElement(Long id);
    public Collection<Mpa> getAll();

}
