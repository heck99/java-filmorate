package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

public interface ModelStorage<T> {
    T create(T element);
    T update (T element);
    Optional<T> getElement (Long id);
    Collection<T> getAll();
    boolean delete(Long id);
}
