package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.sevice.MpaService;

import java.util.Collection;

@Slf4j
@RestController()
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private final MpaService service;

    @GetMapping()
    public Collection<Mpa> GetData() {
        log.info("/GET");
        return service.getAll();

    }

    @GetMapping("/{id}")
    public Mpa GetElement(@PathVariable Long id) {
        log.info("/GET /{}", id);
        return service.getElement(id);
    }
}
