package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends ModelStorage<User>{

    void addFriend(Long firstId, Long secondId);
    Collection<User> getFriends(Long id);
    void deleteFriend(Long id,Long delId);
    Collection<User> getCommonFriends(Long firstId, Long secondId);
}
