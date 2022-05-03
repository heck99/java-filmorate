package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DataDoesNotExistsException;
import ru.yandex.practicum.filmorate.model.DefaultModel;
import ru.yandex.practicum.filmorate.storage.ModelStorage;

import java.util.Collection;

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
        return storage.getElement(id);
    }

    public Collection<V> getAll() {
        log.info("обращаемся к хранилищу");
        return storage.getAll();
    }

    public V putElement(V element) {
        log.info("обращаемся к хранилищу с элементом: " + element.toString());
        isValid(element);
        if(element.getId() != null) {
            if(storage.getElement(element.getId()) != null) {
                storage.update(element);
                log.info("Data info has been updated");
            } else {
                log.warn("Data with id " + element.getId() + " doesn't exist");
                throw new DataDoesNotExistsException("Data with this id doesn't exist");
            }
        } else {
            storage.create(element);
            log.info("New data has been added: " + element.toString());
        }
        return element;
    }

    protected abstract boolean isValid(V element);
}
