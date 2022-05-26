package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
@Slf4j
public class UserService extends ModelService<User, UserStorage> {

    @Autowired
    public UserService(UserStorage userStorage) {
        log.info("Обращаемся к хранилищу");
        this.storage = userStorage;
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Обращаемся к хранилищу");
        storage.addFriend(id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        log.info("Обращаемся к хранилищу");
        return storage.getFriends(id);
    }

    public void deleteFriend(Long id, Long delId) {
        log.info("Обращаемся к хранилищу");
        storage.deleteFriend(id, delId);
    }

    public Collection<User> getCommonFriends(Long firstId, Long secondId) {
        log.info("Обращаемся к хранилищу");
        return storage.getCommonFriends(firstId, secondId);
    }


    @Override
    protected boolean isValid(User user) {
        String email = user.getEmail();
        if(email.isBlank()) {
            log.warn("Email is blank: " + email);
            throw new ValidationException("Email is blank");
        }

        if(!email.contains("@")) {
            log.warn("Email does not match the pattern: " + email);
            throw new ValidationException("Email does not match the pattern");
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

        if(user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return true;
    }
}
