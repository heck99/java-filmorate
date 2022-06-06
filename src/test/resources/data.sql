INSERT INTO USERS (email, login, name, birthday )
VALUES ('email1@mail.ru', 'login1', 'name1', '2000-10-10'),
       ('email2@mail.ru', 'login2', 'name2', '2000-10-10'),
       ('email3@mail.ru', 'login3', 'name3', '2000-10-10'),
       ('email4@mail.ru', 'login4', 'name4', '2000-10-10');

INSERT INTO FRIENDSHIP_STATUS (name)
VALUES ('accept'), ('request');

INSERT INTO FRIENDS (interrogator_id, defendant_id, friendship_status_id)
VALUES ( 1, 2, 2),
       (4, 1, 1),
       (2, 3, 1);

INSERT INTO mpa ( name, description)
VALUES ( 'G', 'у фильма нет возрастных ограничений'),
       ( 'PG', 'детям рекомендуется смотреть фильм с родителями'),
       ( 'PG-13', 'детям до 13 лет просмотр не желателен'),
       ( 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       ( 'NC-17', 'лицам до 18 лет просмотр запрещён');

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ( 'name1', 'description1', '2000-10-10', 100, 1),
       ( 'name2', 'description2', '2000-10-10', 100, 2),
       ( 'name3', 'description3', '2000-10-10', 100, 3);

INSERT INTO likes (film_id, user_id)
VALUES (1, 1),
       (2, 2),
       (2, 1),
       (3, 1),
       (3, 2),
       (3, 3)
