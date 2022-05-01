package ru.yandex.practicum.filmorate.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

//да, я читал про другие, но в ТЗ советовалось использовать анотацию @Data, хотя Марк рассказывал, что её не любят


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Film extends DefaultModel{
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private Integer duration;

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }




}
