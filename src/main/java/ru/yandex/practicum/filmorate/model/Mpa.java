package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mpa extends DefaultModel {
    private String name;
    private String description;

    public Mpa(Long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public Mpa() {
    }

}