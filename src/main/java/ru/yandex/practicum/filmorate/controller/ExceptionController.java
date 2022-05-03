package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler({ValidationException.class, FriendAlreadyExistsException.class, FilmAlreadyLiked.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String onInternalServerError(RuntimeException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({FilmNotFoundException.class, UserNotFoundException.class, NoFriendException.class, NoLikeException.class, DataDoesNotExistsException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onNotFoundError(RuntimeException e) {
        return e.getMessage();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public List<Violation> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return violations;
    }

    private class Violation {
        private final String fieldName;
        private final String message;

        public String getFieldName() {
            return fieldName;
        }

        public String getMessage() {
            return message;
        }

        public Violation(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }
    }
}
