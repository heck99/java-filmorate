package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sevice.UserService;

import java.util.Collection;


@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController extends DefaultController<UserService, User>{

    public UserController(UserService service) {
        super(service);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info(String.format("/PUT /users/%d/friends/%d", id, friendId));
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info(String.format("/DELETE /users/%d/friends/%d", id, friendId));
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        log.info(String.format("/GET /users/%d/friends", id));
        return  service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info(String.format("/GET /users/%d1/friends/common/%d2", id, otherId));
        return service.getCommonFriends(id, otherId);
    }
}