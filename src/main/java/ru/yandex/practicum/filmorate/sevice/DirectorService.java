package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collection;

@Service
@Slf4j
public class DirectorService extends ModelService<Director, DirectorStorage> {

    public DirectorService(DirectorStorage storage) {
        this.storage = storage;
    }

    public Collection<Director> getAllByFilmId(long filmId) {
        log.info("Обращаемся к хранилищу");
        return storage.getAllByFilmId(filmId);
    }

    @Override
    protected boolean isValid(Director element) {
        if (element.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        return true;
    }
}
