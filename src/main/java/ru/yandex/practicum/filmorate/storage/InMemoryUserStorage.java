package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{

    Map<Long, User> users;

    public InMemoryUserStorage() {
       users = new HashMap<>();
    }

    @Override
    public User create(User element) {
        element.setId(Long.valueOf(users.size()));
        users.put(element.getId(), element);
        return element;
    }

    /*как правильно делать обновление просто положить элемент в мапу, и он заменит
    хранящийся там или получить элемент по id и обновить его поля?*/
    @Override
    public User update(User element) {
        users.put(element.getId(), element);
        return element;
    }

    @Override
    public User getElement(Long id) {
       return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
