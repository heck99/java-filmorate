package ru.yandex.practicum.filmorate.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User
{
    private Integer id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;

    public User(String name, String email, String login, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}
