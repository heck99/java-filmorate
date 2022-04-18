package ru.yandex.practicum.filmorate.models;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User extends DefaultModel
{
    private Integer id;
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String login;
    @Past
    @NotNull
    private LocalDate birthday;

    public User(String name, String email, String login, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}
