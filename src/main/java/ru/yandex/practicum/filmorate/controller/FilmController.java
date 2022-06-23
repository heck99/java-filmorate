package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.sevice.FilmService;
import ru.yandex.practicum.filmorate.sevice.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends DefaultController<FilmService, Film> {

    UserService userService;

    @Autowired
    public FilmController(FilmService service, UserService userService) {
        super(service);
        this.userService = userService;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info(String.format("/PUT /%d1/like/%d2", id, userId));
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info(String.format("/DELETE /%d1/like/%d2", id, userId));
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info(String.format("/GET /popular count = %d", count));
        return service.getPopular(count);
    }

    @GetMapping("/common")
    public Collection<Film> getCommon(@RequestParam() long userId,
                                      @RequestParam() long friendId) {
        log.info(String.format("/GET /common userId = %d, friendId = %d", userId, friendId));
        return service.getCommon(userId, friendId);
    }
}
