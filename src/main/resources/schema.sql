create table if not exists genres
(
    genre_id INTEGER auto_increment,
    name   CHARACTER VARYING,
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table if not exists friendship_status
(
    friendship_status_id INTEGER auto_increment,
    name                 CHARACTER VARYING,
    constraint FRIENDSHIP_STATUS_PK
        primary key (friendship_status_id)
);

create unique index if not exists friendship_status_name_uindex
    on friendship_status (name);

create table if not exists mpa
(
    mpa_id      INTEGER auto_increment,
    name        CHARACTER VARYING,
    description CHARACTER VARYING,
    constraint MPA_PK
        primary key (mpa_id)
);



create table if not exists films
(
    film_id      INTEGER auto_increment,
    name         CHARACTER VARYING,
    description  CHARACTER VARYING,
    release_date DATE,
    duration     INTEGER,
    mpa_id       INTEGER,
    constraint FILMS_PK
        primary key (film_id),
    constraint MPA___FK
        foreign key (mpa_id) references MPA
);

create table  if not exists film_genre
(
    film_id  INTEGER not null,
    genre_id INTEGER not null,
    constraint FILM_GENRE_PK
        primary key (genre_id, film_id),
    constraint FILM__FK
        foreign key (film_id) references FILMS,
    constraint GENRE___FK_2
        foreign key (genre_id) references GENRES
);

create table if not exists users
(
    user_id  INTEGER auto_increment,
    login    CHARACTER VARYING,
    email    CHARACTER VARYING,
    name     CHARACTER VARYING,
    birthday DATE,
    constraint USERS_PK
        primary key (user_id)
);

create table if not exists friends
(
    interrogator_id      INTEGER not null,
    defendant_id         INTEGER not null,
    friendship_status_id INTEGER,
    constraint FRIENDS_PK
        primary key (defendant_id, interrogator_id),
    constraint "first___FK"
        foreign key (interrogator_id) references users,
    constraint "friendship_status___FK"
        foreign key (friendship_status_id) references friendship_status,
    constraint "second___FK"
        foreign key (defendant_id) references users
);

create table if not exists likes
(
    film_id INTEGER not null,
    user_id INTEGER not null,
    constraint LIKES_PK
        primary key (user_id, film_id),
    constraint "film___FK"
        foreign key (film_id) references films,
    constraint "user___FK"
        foreign key (user_id) references users
);
