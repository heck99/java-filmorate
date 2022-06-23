package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.yandex.practicum.filmorate.controller.DefaultController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Review extends DefaultModel {
    @NotNull
    @NotBlank
    private String content;
    @NotNull
    @JsonProperty("isPositive")
    private Boolean isPositive;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private int useful;

    public Review(Long id, String content, Boolean isPositive, Long userId, Long filmId, int useful) {
        super(id);
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }
}

