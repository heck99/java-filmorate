package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyLiked extends RuntimeException{
    public FilmAlreadyLiked(String message) {
        super(message);
    }
}
