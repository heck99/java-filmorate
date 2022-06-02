DELETE FROM FRIENDSHIP_STATUS;
DELETE FROM MPA;
DELETE FROM GENRES;

INSERT INTO FRIENDSHIP_STATUS (NAME)
VALUES ('accept'), ('request');

INSERT INTO MPA ( NAME, DESCRIPTION)
VALUES ( 'G', 'у фильма нет возрастных ограничений'),
       ( 'PG', 'детям рекомендуется смотреть фильм с родителями'),
       ( 'PG-13', 'детям до 13 лет просмотр не желателен'),
       ( 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       ( 'NC-17', 'лицам до 18 лет просмотр запрещён');

INSERT INTO GENRES (G_NAME)
VALUES ( 'драмма' ),
       ('боевик'),
       ('комедия'),
       ('мультфильм'),
       ('триллер'),
       ('документальный');