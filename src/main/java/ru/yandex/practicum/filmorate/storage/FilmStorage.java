package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage extends ModelStorage<Film>{
    Collection<Film> getPopular(int count, Optional<Integer> genreId, Optional<Integer> year);
    Collection<Film> getCommon(Long id, Long secondId);
    Collection<Film> getByDirectorIdSortByLikes(long directorId);
    Collection<Film> getByDirectorIdSortByYear(long directorId);
    Collection<Film> searchByDirector(String text);
    Collection<Film> searchByTitle(String text);
    Collection<Film> searchByTitleAndDescription(String text);
}
