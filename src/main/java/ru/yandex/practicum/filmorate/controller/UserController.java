package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sevice.UserService;



@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController extends DefaultController<UserService, User>{
}