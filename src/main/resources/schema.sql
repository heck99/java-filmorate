create table if not exists GENRES
(
    GENRE_ID INTEGER auto_increment,
    G_NAME   CHARACTER VARYING,
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table if not exists FRIENDSHIP_STATUS
(
    FRIENDSHIP_STATUS_ID INTEGER auto_increment,
    NAME                 CHARACTER VARYING,
    constraint FRIENDSHIP_STATUS_PK
        primary key (FRIENDSHIP_STATUS_ID)
);

create unique index if not exists FRIENDSHIP_STATUS_NAME_UINDEX
    on FRIENDSHIP_STATUS (NAME);

create table if not exists MPA
(
    MPA_ID      INTEGER auto_increment,
    NAME        CHARACTER VARYING,
    DESCRIPTION CHARACTER VARYING,
    constraint MPA_PK
        primary key (MPA_ID)
);



create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING,
    DESCRIPTION  CHARACTER VARYING,
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPA_ID       INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint MPA___FK
        foreign key (MPA_ID) references MPA
);

create table  if not exists FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_PK
        primary key (GENRE_ID, FILM_ID),
    constraint FILM__FK
        foreign key (FILM_ID) references FILMS,
    constraint GENRE___FK_2
        foreign key (GENRE_ID) references GENRES
);

create table if not exists USERS
(
    USER_ID  INTEGER auto_increment,
    LOGIN    CHARACTER VARYING,
    EMAIL    CHARACTER VARYING,
    NAME     CHARACTER VARYING,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists FRIENDS
(
    INTERROGATOR_ID      INTEGER not null,
    DEFENDANT_ID         INTEGER not null,
    FRIENDSHIP_STATUS_ID INTEGER,
    constraint FRIENDS_PK
        primary key (DEFENDANT_ID, INTERROGATOR_ID),
    constraint "first___FK"
        foreign key (INTERROGATOR_ID) references USERS,
    constraint "friendship_status___FK"
        foreign key (FRIENDSHIP_STATUS_ID) references FRIENDSHIP_STATUS,
    constraint "second___FK"
        foreign key (DEFENDANT_ID) references USERS
);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_PK
        primary key (USER_ID, FILM_ID),
    constraint "film___FK"
        foreign key (FILM_ID) references FILMS,
    constraint "user___FK"
        foreign key (USER_ID) references USERS
);