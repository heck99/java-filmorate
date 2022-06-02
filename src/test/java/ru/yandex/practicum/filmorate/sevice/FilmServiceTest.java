package ru.yandex.practicum.filmorate.sevice;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {


    FilmService filmService = new FilmService(new InMemoryFilmStorage(), null);

    @Test
    public void shouldReturnTrueWhenFilmDataIsCorrect() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), 100, new Mpa());
        assertTrue(filmService.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsEmpty() {
        Film film = new Film("", "description", LocalDate.of(2020, 10, 10), 100, new Mpa());
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsSpaces() {
        Film film = new Film("    ", "description", LocalDate.of(2020, 10, 10), 100, new Mpa());
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsNull() {
        Film film = new Film(null, "description", LocalDate.of(2020, 10, 10), 100, new Mpa());
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDescriptionTooLong() {
        Film film = new Film("name", "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",
                LocalDate.of(2020, 10, 10), 100, new Mpa());
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenRealiseDAtteIsTooEarly() {
        Film film = new Film("name", "description", LocalDate.of(1895, 12, 27), 100, new Mpa());
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDurationIsNull() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), null, new Mpa());
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDurationIsNegative() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), -1, new Mpa());
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }


}
