package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User element) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users").usingGeneratedKeyColumns("user_id");
        long userId = simpleJdbcInsert.executeAndReturnKey(userToMap(element)).longValue();
        log.info(String.format("Пользователь с id = %d создан", userId));
        //не знаю нормально ли так возвращать объект или лучше обратиться к методу getElement и веернуть объект из него
        element.setId(userId);
        return element;
    }

    //нормально ли так реализовывать обновление или нужно получать элемент по id обновлять его поля и класть обратно
    @Override
    public User update(User element) {
        String sqlQuery = "update users set LOGIN = ?, email = ?, NAME = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, element.getLogin(), element.getEmail(), element.getName(), element.getBirthday(), element.getId());
        //нормально ли так возвращать элемент или нужно получать его по id и вернуть
        log.info(String.format("Пользователь с id = %d обновлён", element.getId()));
        return element;
    }

    @Override
    public Optional<User> getElement(Long id) {
        String sql = "select * from USERS where USER_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if(rs.next()) {
            log.info("Пользователь с id = {} найден", id);
            return Optional.of(makeUser(rs));
        } else {
            log.info("Пользователь с id = {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public Collection<User> getAll() {
        String sql = "select * from USERS";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        return rowSetToUserList(rs);
    }

    @Override
    public void addFriend(Long interrogator, Long defendant) {
        String sqlRequest = "select f_s.name from FRIENDSHIP_STATUS as f_s JOIN FRIENDS as f " +
                "on f_s.FRIENDSHIP_STATUS_ID = F.FRIENDSHIP_STATUS_ID " +
                "where (f.DEFENDANT_ID = ? and f.INTERROGATOR_ID = ?) OR (f.DEFENDANT_ID = ? and f.INTERROGATOR_ID = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlRequest, interrogator, defendant, defendant, interrogator);
        if (rs.next()){
            String friendshipStatus = rs.getString("name");
            if(friendshipStatus.equals("request")){
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

    @Override
    public Collection<User> getFriends(Long id) {
        String sqlRequest = "SELECT u.*\n" +
                "FROM (SELECT CASE WHEN f.INTERROGATOR_ID = ? THEN f.DEFENDANT_ID WHEN f.DEFENDANT_ID = ? THEN f.INTERROGATOR_ID END AS friends\n" +
                "      FROM friends AS f\n" +
                "      JOIN FRIENDSHIP_STATUS f_s on f.FRIENDSHIP_STATUS_ID = f_s.FRIENDSHIP_STATUS_ID\n" +
                "      WHERE (f.INTERROGATOR_ID = ? OR f.DEFENDANT_ID = ? AND f_s.NAME = 'accept')) as f\n" +
                "    JOIN USERS u ON u.USER_ID =  f.friends";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlRequest, id, id, id, id);
        return rowSetToUserList(rs);
    }


    @Override
    public void deleteFriend(Long id, Long delId) {
        String sqlRequest = "DELETE FROM FRIENDS WHERE (DEFENDANT_ID = ? and INTERROGATOR_ID = ?)" +
                " OR (DEFENDANT_ID = ? and INTERROGATOR_ID = ?)";
        jdbcTemplate.update(sqlRequest, id, delId, delId, id);
    }

    @Override
    public Collection<User> getCommonFriends(Long firstId, Long secondId) {
        String sqlRequest = "SELECT u.* FROM" +
                " (SELECT CASE WHEN f.INTERROGATOR_ID = ? THEN f.DEFENDANT_ID" +
                " WHEN f.DEFENDANT_ID = ? THEN f.INTERROGATOR_ID END AS friends\n" +
                "      FROM friends AS f\n" +
                "      JOIN FRIENDSHIP_STATUS f_s on f.FRIENDSHIP_STATUS_ID = f_s.FRIENDSHIP_STATUS_ID\n" +
                "      WHERE (f.INTERROGATOR_ID = ? AND f.DEFENDANT_ID <> ?" +
                "      OR f.DEFENDANT_ID = ? AND f.INTERROGATOR_ID <> ? AND f_s.NAME = 'accept')) AS f_f" +
                " LEFT JOIN friends AS f ON (f.INTERROGATOR_ID = f_f.friends OR f.DEFENDANT_ID = f_f.friends)" +
                " JOIN USERS U on U.USER_ID = f_f.friends" +
                " WHERE (f.INTERROGATOR_ID = ? OR f.DEFENDANT_ID = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlRequest, firstId, firstId, firstId, secondId,
                firstId, secondId, secondId, secondId);
        return rowSetToUserList(rs);
    }

    private List<User> rowSetToUserList(SqlRowSet rs) {
        List<User> list = new ArrayList<>();
        while (rs.next()) {
            list.add(makeUser(rs));
        }
        log.info("Возвращено {} пользователей",list.size());
        return list;
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("login", user.getLogin());
        values.put("email", user.getEmail());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }

    private User makeUser(SqlRowSet rs){
        Long id = rs.getLong("user_id");
        String login = rs.getString("login");
        String email = rs.getString("email");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, name, email, login, birthday);
    }
}
