package ru.yandex.practicum.filmorate.exceptions;

public class DataDoesNotExistsException extends RuntimeException{
    public DataDoesNotExistsException(String message) {
        super(message);
    }
}
