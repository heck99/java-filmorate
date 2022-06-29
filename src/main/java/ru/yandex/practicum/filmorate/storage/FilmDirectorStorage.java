package ru.yandex.practicum.filmorate.storage;

public interface FilmDirectorStorage {
    void create(Long filmId, Long genreId);
    void delete(Long filmId, Long genreId);
    void deleteAllFilmDirectors(Long filmId);
}
