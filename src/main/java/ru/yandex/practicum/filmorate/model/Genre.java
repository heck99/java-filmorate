package ru.yandex.practicum.filmorate.model;

public class Genre extends DefaultModel{
    String name;

    public Genre(String name) {
        super();
    }

    public Genre(Long id, String name) {
        super(id);
        this.name = name;
    }
}
