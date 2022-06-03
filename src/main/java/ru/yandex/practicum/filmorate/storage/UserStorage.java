package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends ModelStorage<User>{

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long firstId, Long secondId);
}
