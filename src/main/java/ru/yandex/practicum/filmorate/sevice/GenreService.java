package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService extends ModelService<Genre, GenreStorage> {
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public Collection<Genre> getAllByFilmId(Long id) {
        log.info("Обращаемся к хранилищу. id = {}", id);
        return storage.getAllByFilmId(id);
    }

    @Override
    protected boolean isValid(Genre element) {
        if (element.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        return true;
    }
}
