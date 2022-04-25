package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Autowired
    FilmController filmController = new FilmController();
    

        

    @Test
    public void shouldReturnTrueWhenFilmDataIsCorrect() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), 100);
        assertTrue(filmController.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsEmpty() {
        Film film = new Film("", "description", LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> filmController.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsSpaces() {
        Film film = new Film("    ", "description", LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> filmController.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsNull() {
        Film film = new Film(null, "description", LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> filmController.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDescriptionTooLong() {
        Film film = new Film("name", "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",
                LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> filmController.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenRealiseDAtteIsTooEarly() {
        Film film = new Film("name", "description", LocalDate.of(1895, 12, 27), 100);
        assertThrows(ValidationException.class, () -> filmController.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDurationIsNull() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), null);
        assertThrows(ValidationException.class, () -> filmController.isValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDurationIsNegative() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), -1);
        assertThrows(ValidationException.class, () -> filmController.isValid(film));
    }


} 
