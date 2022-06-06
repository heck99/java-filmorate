package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataDoesNotExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
@Slf4j
public class UserService extends ModelService<User, UserStorage> {


    /*Тут я так же решил вставить стораж в сервис пользователей из-за проверки
     на существование пользователей перед добавлением в друзья */
    FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("FriendDbStorage") FriendStorage friendStorage) {
        this.storage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(Long id, Long friendId) {
        log.info(String.format("Проверяем наличие пользователя с id = %d и id = %d", id, friendId));
        if(storage.getElement(id).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", id);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", id));
        }
        if(storage.getElement(friendId).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", friendId);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", friendId));
        }
        log.info("Обращаемся к хранилищу");
        friendStorage.addFriend(id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        log.info("Обращаемся к хранилищу");
        return storage.getFriends(id);
    }

    public void deleteFriend(Long id, Long delId) {
        log.info(String.format("Проверяем наличие пользователя с id = %d и id = %d", id, delId));
        if(storage.getElement(id).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", id);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", id));
        }
        if(storage.getElement(delId).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", delId);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", delId));
        }
        log.info("Обращаемся к хранилищу");
        friendStorage.deleteFriend(id, delId);
    }

    public Collection<User> getCommonFriends(Long firstId, Long secondId) {

        log.info(String.format("Проверяем наличие пользователя с id = %d и id = %d", firstId, secondId));
        if(storage.getElement(firstId).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", firstId);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", firstId));
        }
        if(storage.getElement(secondId).isEmpty()) {
            log.info("Пользователь с  id = {} не найден", secondId);
            throw new DataDoesNotExistsException(String.format("Пользователь с  id = %d не найден", secondId));
        }
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
