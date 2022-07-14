package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.DefaultModel;
import ru.yandex.practicum.filmorate.sevice.ModelService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
public abstract class DefaultController<V extends ModelService<T, ?>, T extends DefaultModel> {
    V service;

    public DefaultController(V service) {
        this.service = service;
    }

    @GetMapping()
    public Collection<T> GetData() {
        log.info("/GET");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public T GetElement(@PathVariable Long id) {
        log.info("/GET /{}", id);
        return service.getElement(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("/Delete /{}", id);
        service.delete(id);
    }

    @PostMapping
    public T createData(@Valid @RequestBody T element) {
        log.info("/POST: " + element.toString());
        return service.create(element);
    }

    @PutMapping
    public T updateData (@Valid @RequestBody T element) {
        log.info("/PUT: " + element.toString());
        return service.putElement(element);
    }

}
