package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.sevice.FilmService;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends DefaultController<FilmService, Film> {

}
