package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    Map<Long, Film> films;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    @Override
    public Film create(Film element) {
        element.setId(Long.valueOf(films.size()));
        films.put(element.getId(), element);
        return element;
    }

    @Override
    public Film update(Film element) {
        films.put(element.getId(), element);
        return element;
    }

    @Override
    public Film getElement(Long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }
}
