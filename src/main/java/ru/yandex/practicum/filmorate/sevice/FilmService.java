package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class FilmService extends ModelService<Film, FilmStorage> {

    UserService userService;
    LikeStorage likeStorage;
    GenreService genreService;
    FilmGenreStorage FGStorage;

    DirectorService directorService;
    FilmDirectorStorage FDStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       UserService userService,
                       @Qualifier("LikeDbStorage") LikeStorage likeStorage,
                       GenreService genreService,
                       FilmGenreStorage FGStorage,
                       DirectorService directorService,
                       FilmDirectorStorage FDStorage) {
        this.storage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
        this.genreService = genreService;
        this.FGStorage = FGStorage;
        this.directorService = directorService;
        this.FDStorage = FDStorage;
    }

    public void addLike(Long filmId, Long userId) {
        log.info(String.format("Проверяем наличие пользователя с id = %d и фильма с id = %d", userId, filmId));
        userService.getElement(userId);
        getElement(filmId);

        log.info(String.format("Пользователь с  id = %d и фильм с id = %d существуют, обращаемся к хранилищу фильмов", userId, filmId));
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.info(String.format("Проверяем наличие пользователя с id = %d и фильма с id = %d", userId, filmId));
        userService.getElement(userId);
        getElement(filmId);
        log.info(String.format("Пользователь с  id = %d и фильм с id = %d существуют, обращаемся к хранилищу фильмов", userId, filmId));
        likeStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopular(int count, Optional<Integer> genreId, Optional<Integer> year) {
        log.info("Обращаемся к хранилищу фильмов");
        if (count <= 0) {
            log.warn("FindPopular. Передан неверный параметр count {}", count);
            throw new ValidationException("Передан неверный параметр count");
        }
        Collection<Film> films = storage.getPopular(count, genreId, year);
        setGenreForFilmCollection(films);
        setDirectorsForFilmCollection(films);
        return films;
    }


    public Collection<Film> getCommon(long id, long secondId) {
        log.info("Обращаемся к хранилищу фильмов");
        userService.getElement(id);
        userService.getElement(secondId);
        Collection<Film> films = storage.getCommon(id, secondId);
        setGenreForFilmCollection(films);
        setDirectorsForFilmCollection(films);
        return films;
    }

    public Collection<Film> search(String text, String by) {
        Collection<Film> films = new ArrayList<>();
        if(by.contains("title") && by.contains("director")) {
            films.addAll(storage.searchByTitleAndDescription(text));
        } else {
            if (by.contains("title")) {
                films.addAll(storage.searchByTitle(text));
            }
            if (by.contains("director")) {
                films.addAll(storage.searchByDirector(text));
            }
        }
        setGenreForFilmCollection(films);
        setDirectorsForFilmCollection(films);
        return films;
    }

    @Override
    public Film create(Film element) {
        Film film = super.create(element);
        Collection<Genre> genres = element.getGenres();
        Collection<Director> directors = element.getDirectors();
        if (genres != null) {
            for (Genre genre : genres) {
                FGStorage.create(film.getId(), genre.getId());
            }
            film.addAllGenre(genres);
        }
        for (Director director : directors) {
            FDStorage.create(film.getId(), director.getId());
        }
        film.addAllDirectors(directors);

        return film;
    }

    @Override
    public Film getElement(Long id) {
        Film film = super.getElement(id);
        setFilmGenre(film);
        setFilmDirectors(film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = super.getAll();
        setGenreForFilmCollection(films);
        setDirectorsForFilmCollection(films);
        return films;
    }

    @Override
    public Film putElement(Film element) {
        Film film = super.putElement(element);
        FGStorage.deleteAllFilmGenre(film.getId());
        FDStorage.deleteAllFilmDirectors(film.getId());
        Collection<Genre> genres = element.getGenres();
        Collection<Director> directors = element.getDirectors();
        if (genres != null) {
            for (Genre genre : genres) {
                FGStorage.create(film.getId(), genre.getId());
            }
            film.addAllGenre(genres);
        }
        if (directors.size() > 0) {
            for (Director director : directors) {
                FDStorage.create(film.getId(), director.getId());
            }
            film.addAllDirectors(directors);
        } else {
            film.setDirectors(null);
        }
        return film;
    }

    public Collection<Film> getByDirectorId(long directorId, String sort) {
        directorService.getElement(directorId);
        Collection<Film> films;
        switch (sort) {
            case "year":
                films = storage.getByDirectorIdSortByYear(directorId);
                break;
            case "likes":
                films = storage.getByDirectorIdSortByLikes(directorId);
                break;
            default:
                throw new ValidationException("введён неверный параметр sortBy");
        }
        setGenreForFilmCollection(films);
        setDirectorsForFilmCollection(films);
        return films;
    }

    private void setGenreForFilmCollection(Collection<Film> films) {
        for (Film film : films) {
            setFilmGenre(film);
        }
    }

    private void setFilmGenre(Film film) {
        Collection<Genre> genres = genreService.getAllByFilmId(film.getId());
        if (genres.size() > 0) {
            film.addAllGenre(genres);
        }
    }

    private void setDirectorsForFilmCollection(Collection<Film> films) {
        for (Film film : films) {
            setFilmDirectors(film);
        }
    }

    private void setFilmDirectors(Film film) {
        Collection<Director> directors = directorService.getAllByFilmId(film.getId());
        film.addAllDirectors(directors);
    }

    @Override
    protected boolean isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Movie name is empty");
            throw new ValidationException("Movie name is empty");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Movie description is too long");
            throw new ValidationException("Movie description is too long");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Movie release data is too early: " + film.getReleaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            throw new ValidationException("Movie release data is too early");
        }

        if (film.getDuration() == null) {
            log.warn("Movie duration is not specify");
            throw new ValidationException("Movie duration is not specify");
        }

        if (film.getDuration() < 0) {
            log.warn("Movie duration is negative: " + film.getDuration());
            throw new ValidationException("Movie duration is negative");
        }

        return true;
    }
}
