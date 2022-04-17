package ru.yandex.practicum.filmorate.functionals;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    public void shouldReturnTrueWhenFilmDataIsCorrect() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), 100);
        assertTrue(Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsEmpty() {
        Film film = new Film("", "description", LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsSpaces() {
        Film film = new Film("    ", "description", LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenNameIsNull() {
        Film film = new Film(null, "description", LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDescriptionTooLong() {
        Film film = new Film("name", "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",
                LocalDate.of(2020, 10, 10), 100);
        assertThrows(ValidationException.class, () -> Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenRealiseDAtteIsTooEarly() {
        Film film = new Film("name", "description", LocalDate.of(1895, 12, 27), 100);
        assertThrows(ValidationException.class, () -> Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDurationIsNull() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), null);
        assertThrows(ValidationException.class, () -> Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDurationIsNegative() {
        Film film = new Film("name", "description", LocalDate.of(2020, 10, 10), -1);
        assertThrows(ValidationException.class, () -> Validator.filmIsValid(film));
    }

    @Test
    public void shouldReturnTrueWhenUserDataIsCorrect() {
        User user = new User("name", "email@mail.ru", "login", LocalDate.of(1999, 6, 28));
        assertTrue(Validator.userIsValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailDoNotAt() {
        User user = new User("name", "emailmail.ru", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> Validator.userIsValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailIsSpaces() {
        User user = new User("name", "    ", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> Validator.userIsValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailIsEmpty() {
        User user = new User("name", "", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> Validator.userIsValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenLoginIsEmpty() {
        User user = new User("name", "email@mail.ru", "", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> Validator.userIsValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDateBirthdayIsNull() {
        User user = new User("name", "email@mail.ru", "12 dsf", null);
        assertThrows(ValidationException.class, () -> Validator.userIsValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDateBirthdayIsInFuture() {
        User user = new User("name", "email@mail.ru", "12dsf", LocalDate.of(4040, 6, 28));
        assertThrows(ValidationException.class, () -> Validator.userIsValid(user));
    }
}