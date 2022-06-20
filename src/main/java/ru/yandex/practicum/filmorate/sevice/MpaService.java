package ru.yandex.practicum.filmorate.sevice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataDoesNotExistsException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class MpaService {
    MpaStorage storage;

    public Mpa getElement(Long id) {
        Optional<Mpa> element = storage.getElement(id);
        if(element.isEmpty()) {
            log.info("Объект с id = {} не найден", id);
            throw new DataDoesNotExistsException(String.format("Объект с id = %d не найден", id));
        }
        return element.get();
    }

    public Collection<Mpa> getAll() {
        log.info("обращаемся к хранилищу");
        return storage.getAll();
    }

}
