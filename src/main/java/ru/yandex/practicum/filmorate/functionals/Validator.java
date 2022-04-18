package ru.yandex.practicum.filmorate.functionals;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Validator {

    public static boolean userIsValid(User user) {
        String email = user.getEmail();
        if(!email.contains("@")) {
            log.warn("Email does not match the pattern: " + email);
            throw new ValidationException("Email does not match the pattern");
        }
        if(email.isBlank()) {
            log.warn("Email is blank: " + email);
            throw new ValidationException("Email is blank");
        }
        String login = user.getLogin();
        if(login.isBlank()) {
            log.warn("Login is blank: " + login);
            throw new ValidationException("Login is blank");
        }

        if(login.contains(" ")) {
            log.warn("Login contains spaces: " + login);
            throw new ValidationException("Login contains spaces");
        }

        LocalDate dateOfBirthday = user.getBirthday();
        if(dateOfBirthday == null) {
            log.warn("Date of Birthday in empty");
            throw new ValidationException("Date of Birthday is empty");
        }
        if(dateOfBirthday.isAfter(LocalDate.now())) {
            log.warn("Date of Birthday in future " + dateOfBirthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "now is: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            throw new ValidationException("Date of Birthday in future");
        }
        return true;
    }

    public static boolean filmIsValid(Film film) {
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
