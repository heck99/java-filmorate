DELETE FROM FRIENDSHIP_STATUS;


INSERT INTO FRIENDSHIP_STATUS (NAME)
VALUES ('accept'), ('request');

CREATE TABLE mpa_ref (
    name varchar,
    description varchar
);

CREATE TABLE genre_ref (
    name varchar
);

CREATE TABLE friendship_status_ref (
    name varchar
);

INSERT INTO friendship_status_ref (NAME)
VALUES ('accept'), ('request');

INSERT INTO mpa_ref (name, description) VALUES ('G', 'у фильма нет возрастных ограничений'),
                                               ('PG', 'детям рекомендуется смотреть фильм с родителями'),
                                               ('PG-13', 'детям до 13 лет просмотр не желателен'),
                                               ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
                                               ('NC-17', 'лицам до 18 лет просмотр запрещён');

MERGE INTO mpa
    USING (mpa_ref) ON (mpa.name = mpa_ref.name)
WHEN NOT MATCHED THEN
    INSERT (name, description) VALUES (mpa_ref.name, mpa_ref.description);

INSERT INTO genre_ref (name) VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

MERGE INTO genres
    USING (genre_ref) ON (genres.name = genre_ref.name)
WHEN NOT MATCHED THEN
    INSERT (name) VALUES (genre_ref.name);

MERGE INTO friendship_status
    USING (friendship_status_ref) ON (friendship_status_ref.name = friendship_status.name)
WHEN NOT MATCHED THEN
    INSERT (name) VALUES (friendship_status_ref.name);


DROP TABLE mpa_ref;
DROP TABLE genre_ref;
DROP TABLE friendship_status_ref;