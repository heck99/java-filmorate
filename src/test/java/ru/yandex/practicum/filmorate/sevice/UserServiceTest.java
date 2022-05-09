package ru.yandex.practicum.filmorate.sevice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Autowired
    UserService userService= new UserService(new InMemoryUserStorage());

    @Test
    public void shouldReturnTrueWhenUserDataIsCorrect() {
        User user = new User("name", "email@mail.ru", "login", LocalDate.of(1999, 6, 28));
        assertTrue(userService.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailDoNotAt() {
        User user = new User("name", "emailmail.ru", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailIsSpaces() {
        User user = new User("name", "    ", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenEmailIsEmpty() {
        User user = new User("name", "", "login", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenLoginIsEmpty() {
        User user = new User("name", "email@mail.ru", "", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenLoginContainsSpaces() {
        User user = new User("name", "email@mail.ru", "12 dsf", LocalDate.of(1999, 6, 28));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDateBirthdayIsNull() {
        User user = new User("name", "email@mail.ru", "12dsf", null);
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldReturnValidatorExceptionWhenDateBirthdayIsInFuture() {
        User user = new User("name", "email@mail.ru", "12dsf", LocalDate.of(4040, 6, 28));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }







}