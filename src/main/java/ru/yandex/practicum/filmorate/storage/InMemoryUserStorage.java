package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NoFriendException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{

    Map<Long, User> users;

    HashMap<Long, Set<Long>> friends;

    public InMemoryUserStorage() {
       users = new HashMap<>();
       friends = new HashMap<>();
    }

    @Override
    public User create(User element) {
        element.setId(Long.valueOf(users.size() + 1));
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
        User user = users.get(id);
        if(user == null) {
            throw new UserNotFoundException(String.format("Пользователь c id: %s не найден", id));
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void addFriend(Long firstId, Long secondId) {
        //проверяем существование пользователей
        if(!users.containsKey(firstId)) {
            throw new UserNotFoundException(String.format("Пользователь c id: %d не найден", firstId));
        }
        if(!users.containsKey(secondId)) {
            throw new UserNotFoundException(String.format("Пользователь c id: %d не найден", secondId));
        }
        //если у пользователей ещё нет друзей создаём для них сет в мапе
        if(!friends.containsKey(firstId)) {
            friends.put(firstId, new HashSet<>());
        }
        if(!friends.containsKey(secondId)) {
            friends.put(secondId, new HashSet<>());
        }
        //добавляем в друзья, если уже в друзьях то кидаем исключение
        boolean answer = friends.get(firstId).add(secondId);
        if(!answer) {
            throw new FriendAlreadyExistsException(String.format("Пользователь с id = %d1 уже имеет друга с id = %d2", firstId, secondId));
        }
        answer = friends.get(secondId).add(firstId);
        if(!answer) {
            throw new FriendAlreadyExistsException(String.format("Пользователь с id = %d1 уже имеет друга с id = %d2", secondId, firstId));
        }
    }

    @Override
    public Collection<User> getFriends(Long id) {
        if(!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь c id: %d не найден", id));
        }
        Collection<User> userFriends = new ArrayList<>();
        for (Long friendsId : friends.get(id)) {
         userFriends.add(users.get(friendsId));
        }
        return userFriends;
    }

    @Override
    public void deleteFriend(Long id, Long delId) {
        if(!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь c id: %d не найден", id));
        }
        if(!friends.containsKey(id)) {
            throw new NoFriendException(String.format("У пользователя с id = %id нет друзей", id));
        }
        Set<Long> friendsId = friends.get(id);
        if(!friendsId.remove(delId)) {
            throw new NoFriendException(String.format("У пользователя с id = %d1 нет друга с id = %d2", id, delId));
        }
    }

    @Override
    public Collection<User> getCommonFriends(Long firstId, Long secondId) {
        if(!users.containsKey(firstId)) {
            throw new UserNotFoundException(String.format("Пользователь c id: %d не найден", firstId));
        }
        if(!users.containsKey(secondId)) {
            throw new UserNotFoundException(String.format("Пользователь c id: %d не найден", secondId));
        }

        //если хотя бы у одного пользователя нет друзей, то и пересечение будет пустым
        if(!friends.containsKey(firstId)) {
           return new ArrayList<>();
        }
        if(!friends.containsKey(secondId)) {
            return new ArrayList<>();
        }

        Collection<Long> intersection = new HashSet<>(friends.get(firstId));
        intersection.retainAll(friends.get(secondId));

        Collection<User> toReturn = new ArrayList<>();
        for (Long friendsId : intersection) {
            toReturn.add(users.get(friendsId));
        }
        return toReturn;
    }
}
