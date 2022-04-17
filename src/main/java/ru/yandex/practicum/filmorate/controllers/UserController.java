package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.functionals.Validator;
import ru.yandex.practicum.filmorate.models.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {

    HashMap<Integer, User> users = new HashMap<>();

    @GetMapping()
    public Collection<User> GetUsers() {
        log.info("/GET");
        return users.values();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.info("/POST: " + user.toString());
        /*возможно правильнее не кидать исключение в валидаторе, а получать результат false и в этом методе бросать
        исключения, но я не совсем придумал, как тогда различать какое поле не прошло валидацию, был вариант создать
        отдельно исключение для ошибки в логине пароле и пр. возможно так лучше*/
        /*То что ниже было сделано для первой частизадания, которое без звёздочки, но когда я сделал задание со
        звездочкой, то у меня не получилось сделать валидацию на дату релиза фильма с помощью антоаций, поэтому я решил
         оставить обе валидации*/
        Validator.userIsValid(user);
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        /*Может не совсем корректно присваивать id через количество элементов в мапе, но так как у нас это временное
        хранилеще, видимо, на 2 спринта и у нас нет эндпоинта для удаление пользователя, тогда должно работать верно*/
        user.setId(users.size());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User userToUpdate) {
        log.info("/PUT: " + userToUpdate.toString());
        Validator.userIsValid(userToUpdate);
        if(userToUpdate.getName().isBlank() || userToUpdate.getName() == null) {
            userToUpdate.setName(userToUpdate.getLogin());
        }
        if(userToUpdate.getId() != null) {
            if(users.containsKey(userToUpdate.getId())) {
                users.put(userToUpdate.getId(), userToUpdate);
                log.info("User Info has been updated");
            } else {
                log.warn("\"User with id " + userToUpdate.getId() + " doesn't exist");
                throw new UserDoesNotExistException("User with this id doesn't exist");
            }
        } else {
            userToUpdate.setId(users.size());
            users.put(userToUpdate.getId(), userToUpdate);
            log.info("New user has been added: " + userToUpdate.toString());
        }
        return userToUpdate;
    }

}