package ru.yandex.practicum.filmorate.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Film extends DefaultModel{
    @NotEmpty
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private Integer duration;
    @NotNull
    private Mpa mpa;

    private Set<Genre> genres;
    private Set<Director> directors = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
    public Film(){
        super();
    }

    public Film(long id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
    public void addAllGenre(Collection<Genre> genres) {
        if(this.genres == null) {
            this.genres = new HashSet<>();
        }
        this.genres.addAll(genres);
    }

    public void addAllDirectors(Collection<Director> directors) {
        this.directors.addAll(directors);
    }

}
