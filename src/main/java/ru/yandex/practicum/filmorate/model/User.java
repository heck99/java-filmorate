package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends DefaultModel
{
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

    public User(Long id, String name, String email, String login, LocalDate birthday) {
        super(id);
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public User() {
    }

}
