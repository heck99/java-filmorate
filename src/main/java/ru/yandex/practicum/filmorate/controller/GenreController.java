package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.sevice.GenreService;


@RestController
@RequestMapping("/genres")
public class GenreController extends DefaultController<GenreService, Genre> {
    public GenreController(GenreService service) {
        super(service);
    }
}
