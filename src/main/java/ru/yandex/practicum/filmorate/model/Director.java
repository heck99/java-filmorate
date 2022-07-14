package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;

public class Director extends DefaultModel{
    @NotBlank
    private String name;

    public Director() {
        super();
    }

    public Director(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
