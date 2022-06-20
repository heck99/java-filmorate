package ru.yandex.practicum.filmorate.sevice;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
public class GenreService extends ModelService<Genre, GenreStorage> {
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public Collection<Genre> getAllByFilmId(Long id) {
        return storage.getAllByFilmId(id);
    }

    @Override
    protected boolean isValid(Genre element) {
        return !element.getName().isBlank();
    }
}
