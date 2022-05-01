package ru.yandex.practicum.filmorate.exception;

public class DataDoesNotExistsException extends RuntimeException{
    public DataDoesNotExistsException(String message) {
        super(message);
    }
}
