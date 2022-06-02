package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataDoesNotExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
@Slf4j
public class FilmService extends ModelService<Film, FilmStorage> {

    UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("UserDbStorage") UserStorage userStorage ) {
        this.storage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        log.info(String.format("Проверяем наличие пользователя с id = %d и фильма с id = %d", userId, filmId));

        if(userStorage.getElement(userId).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", userId);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", userId));
        }
        if(storage.getElement(filmId).isEmpty()) {
            log.info("Фильм с  id = {} не не найден", filmId);
            throw new DataDoesNotExistsException(String.format("Фильм с  id = %d не найден", filmId));
        }

        log.info(String.format("Пользователь с  id = %d и фильм с id = %d существуют, обращаемся к хранилищу фильмов", userId, filmId));
        storage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.info(String.format("Проверяем наличие пользователя с id = %d и фильма с id = %d", userId, filmId));
        if(userStorage.getElement(userId).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", userId);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", userId));
        }
        if(storage.getElement(filmId).isEmpty()) {
            log.info("Фильм с  id = {} не не найден", filmId);
            throw new DataDoesNotExistsException(String.format("Фильм с  id = %d не найден", filmId));
        }
        log.info(String.format("Пользователь с  id = %d и фильм с id = %d существуют, обращаемся к хранилищу фильмов", userId, filmId));
        storage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopular(int count) {
        log.info("Обращаемся к хранилищу фильмов");
        return storage.getPopular(count);
    }

    @Override
    protected boolean isValid(Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            log.warn("Movie name is empty");
            throw new ValidationException("Movie name is empty");
        }

        if(film.getDescription().length() >200) {
            log.warn("Movie description is too long");
            throw new ValidationException("Movie description is too long");
        }

        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12,28))) {
            log.warn("Movie release data is too early: " + film.getReleaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) );
            throw new ValidationException("Movie release data is too early");
        }

        if(film.getDuration() == null) {
            log.warn("Movie duration is not specify");
            throw new ValidationException("Movie duration is not specify");
        }

        if(film.getDuration() < 0) {
            log.warn("Movie duration is negative: " + film.getDuration());
            throw new ValidationException("Movie duration is negative");
        }

        return true;
    }
}
