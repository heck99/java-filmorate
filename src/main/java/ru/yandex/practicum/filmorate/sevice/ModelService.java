package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DataDoesNotExistsException;
import ru.yandex.practicum.filmorate.model.DefaultModel;
import ru.yandex.practicum.filmorate.storage.ModelStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
public abstract class ModelService<V extends DefaultModel, T extends ModelStorage<V>> {

    T storage;

    public V create(V element) {
        isValid(element);
        log.info("обращаемся к хранилищу" + element.toString());
        storage.create(element);
        return element;
    }

    public V getElement(Long id) {
        Optional<V> element = storage.getElement(id);
        if(element.isEmpty()) {
            throw new DataDoesNotExistsException(String.format("Объект с id = %d не найден", id));
        }
        return element.get();
    }

    public Collection<V> getAll() {
        log.info("обращаемся к хранилищу");
        return storage.getAll();
    }


    //TODO:разобраться с фигнёй
    public V putElement(V element) {
        log.info("обращаемся к хранилищу с элементом: " + element.toString());
        isValid(element);
        if(element.getId() != null) {
            if(storage.getElement(element.getId()).isPresent()) {
                storage.update(element);
                log.info("Data info has been updated");
            } else {
                log.warn("Data with id " + element.getId() + " doesn't exist");
                throw new DataDoesNotExistsException("Data with this id doesn't exist");
            }
        } else {
            storage.create(element);
            log.info("New data has been added: " + element);
        }
        return element;
    }

    protected abstract boolean isValid(V element);
}
