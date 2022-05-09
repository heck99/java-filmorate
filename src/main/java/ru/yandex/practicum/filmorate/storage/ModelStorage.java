package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface ModelStorage<T> {
    T create(T element);
    T update (T element);
    T getElement (Long id);
    Collection<T> getAll();
}
