package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    Map<Long, Film> films;
    Map<Long, Set<Long>> likes;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
        likes = new HashMap<>();
    }

    @Override
    public Film create(Film element) {
        element.setId((long) films.size() + 1);
        films.put(element.getId(), element);
        likes.put(element.getId(), new HashSet<>());
        return element;
    }

    @Override
    public Film update(Film element) {
        films.put(element.getId(), element);
        return element;
    }

    @Override
    public Optional<Film> getElement(Long id) {
        Film film = films.get(id);
        if(film == null) {
            return Optional.empty();
        }
        return Optional.of(film);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }


    @Override
    public void addLike(Long filmId, Long userId) {
        //проверяем существование фильма
        if(!likes.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм c id: %d не найден", filmId));
        }

        //добавляем лайк, если уже поставлен кидаем исключение
        boolean answer = likes.get(filmId).add(userId);
        if(!answer) {
            throw new FilmAlreadyLiked(String.format("Пользователь с id = %d1 уже поставил лайк фильму с id = %d2", userId, filmId));
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        if(!likes.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм c id: %d не найден", filmId));
        }

        if(!likes.get(filmId).remove(userId)) {
            throw new NoLikeException(String.format("У фильма с id = %d1 нет лайка от пользователя с id = %d2", filmId, userId));
        }
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return likes.entrySet().stream()
                .sorted((o1, o2) -> Long.compare(o2.getValue().size(), o1.getValue().size()))
                .map(t -> films.get(t.getKey())).limit(count).collect(Collectors.toList());
    }
}
