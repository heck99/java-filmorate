package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    //Не согласен, что должен быть код ошибки 500, если непрошла валидация, но для тестов нужно так
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String onConstraintValidationException(ValidationException e) {
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
