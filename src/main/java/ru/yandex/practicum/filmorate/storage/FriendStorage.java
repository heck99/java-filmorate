package ru.yandex.practicum.filmorate.storage;

public interface FriendStorage{

    void addFriend(Long firstId, Long secondId);

    void deleteFriend(Long id,Long delId);
}
