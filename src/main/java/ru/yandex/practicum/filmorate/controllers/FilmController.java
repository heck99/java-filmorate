package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistsException;
import ru.yandex.practicum.filmorate.functionals.Validator;
import ru.yandex.practicum.filmorate.models.Film;


import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public Collection<Film> GetUsers() {
        log.info("/GET");
        return films.values();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("/POST: " + film.toString());
        //Validator.filmIsValid(film);
        film.setId(films.size());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film putFilm (@Valid @RequestBody Film film) {
        log.info("/PUT: " + film.toString());
        //Validator.filmIsValid(film);
        if(film.getId() != null) {
            if(films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Film info has been updated");
            } else {
                log.warn("\"Film with id " + film.getId() + " doesn't exist");
                throw new FilmDoesNotExistsException("Film with this id doesn't exist");
            }
        } else {
            film.setId(films.size());
            films.put(film.getId(), film);
            log.info("New film has been added: " + film.toString());
        }
        return film;
    }

}
