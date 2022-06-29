package ru.yandex.practicum.filmorate.sevice;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collection;

@Service
public class DirectorService extends ModelService<Director, DirectorStorage>{

    public DirectorService(DirectorStorage storage) {
        this.storage = storage;
    }

    public Collection<Director> getAllByFilmId(long filmId) {
        return storage.getAllByFilmId(filmId);
    }

    @Override
    protected boolean isValid(Director element) {
        return !element.getName().isBlank();
    }
}
