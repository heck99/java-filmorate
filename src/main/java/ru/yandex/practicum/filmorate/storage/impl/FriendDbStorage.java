package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendAlreadyExistsException;
import ru.yandex.practicum.filmorate.storage.FriendStorage;


@Component("FriendDbStorage")
@Slf4j
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void deleteFriend(Long id, Long delId) {
        String sqlRequest = "DELETE FROM FRIENDS WHERE (DEFENDANT_ID = ? and INTERROGATOR_ID = ?)" +
                " OR (DEFENDANT_ID = ? and INTERROGATOR_ID = ?)";
        jdbcTemplate.update(sqlRequest, id, delId, delId, id);
        log.info("Дружба между пользователями с id = {} и id = {} удалена", id, delId);
    }

    @Override
    public void addFriend(Long interrogator, Long defendant) {
        String sqlRequest = "select f_s.name from FRIENDSHIP_STATUS as f_s JOIN FRIENDS as f " +
                "on f_s.FRIENDSHIP_STATUS_ID = F.FRIENDSHIP_STATUS_ID " +
                "where (f.DEFENDANT_ID = ? and f.INTERROGATOR_ID = ?) OR (f.DEFENDANT_ID = ? and f.INTERROGATOR_ID = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlRequest, interrogator, defendant, defendant, interrogator);
        if (rs.next()) {
            String friendshipStatus = rs.getString("name");
            if (friendshipStatus.equals("request")) {
                String sqlRequest3 = "UPDATE FRIENDS SET FRIENDSHIP_STATUS_ID = (SELECT FRIENDSHIP_STATUS_ID" +
                        " FROM FRIENDSHIP_STATUS WHERE NAME = 'accept' LIMIT 1) WHERE INTERROGATOR_ID = ? AND DEFENDANT_ID = ?";
                jdbcTemplate.update(sqlRequest3);
                log.info("Пользователь с id = {} принял запрос в друзья от пользователя с id = {}", interrogator, defendant);
                return;
            }
            log.info("Пользователи уже друзья");
            throw new FriendAlreadyExistsException("Пользователи уже друзья");
        }
        String sqlRequest2 = "insert into FRIENDS (INTERROGATOR_ID, DEFENDANT_ID, FRIENDSHIP_STATUS_ID)" +
                " SELECT ?, ?, FRIENDSHIP_STATUS_ID FROM FRIENDSHIP_STATUS WHERE NAME = 'request' LIMIT 1";
        jdbcTemplate.update(sqlRequest2, interrogator, defendant);
        log.info("Добавлен запрос в друзья пользователю с id = {} от пользователя с id = {}", defendant, interrogator);
    }
}
