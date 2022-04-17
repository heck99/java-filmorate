package ru.yandex.practicum.filmorate.exceptions;

public class FilmDoesNotExistsException extends RuntimeException{
    public FilmDoesNotExistsException(String message) {
        super(message);
    }
}
