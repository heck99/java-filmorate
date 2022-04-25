package ru.yandex.practicum.filmorate.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode
public abstract class DefaultModel {
    @Getter
    @Setter
    protected Integer id;
}
