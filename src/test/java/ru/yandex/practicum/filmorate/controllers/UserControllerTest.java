package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {


    UserController userController = new UserController();
    @Test
    public void shouldReturnTrueWhenUserDataIsCorrect() {
        User user = new User("name", "email@mail.ru", "login", LocalDate.of(1999, 6, 28));
        assertTrue(userController.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailDoNotAt() {
        User user = new User("name", "emailmail.ru", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userController.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailIsSpaces() {
        User user = new User("name", "    ", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userController.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailIsEmpty() {
        User user = new User("name", "", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userController.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenLoginIsEmpty() {
        User user = new User("name", "email@mail.ru", "", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userController.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenLoginContainsSpaces() {
        User user = new User("name", "email@mail.ru", "12 dsf", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userController.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDateBirthdayIsNull() {
        User user = new User("name", "email@mail.ru", "12dsf", null);
        assertThrows(ValidationException.class, () -> userController.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDateBirthdayIsInFuture() {
        User user = new User("name", "email@mail.ru", "12dsf", LocalDate.of(4040, 6, 28));
        assertThrows(ValidationException.class, () -> userController.isValid(user));
    }







}