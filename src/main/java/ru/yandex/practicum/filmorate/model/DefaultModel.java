package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode
public abstract class DefaultModel {
    @Getter
    @Setter
    protected Long id;
}
