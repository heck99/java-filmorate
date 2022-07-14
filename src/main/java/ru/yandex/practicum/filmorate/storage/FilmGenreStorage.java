package ru.yandex.practicum.filmorate.storage;

public interface FilmGenreStorage {
    void create(Long filmId, Long genreId);
    void delete(Long filmId, Long genreId);
    void deleteAllFilmGenre(Long filmId);
}
