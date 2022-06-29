package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.sevice.DirectorService;

@RestController
@RequestMapping("/directors")
public class DirectorController extends DefaultController<DirectorService, Director> {
    public DirectorController(DirectorService service) {
        super(service);
    }
}
