package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;

    private SimpleJdbcInsert insertRole;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");
        this.insertRole = new SimpleJdbcInsert(dataSource)
                .withTableName("USER_ROLES");
    }

    @Override
    @Transactional
    public User save(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());
            user.getRoles().forEach(role -> {
                MapSqlParameterSource rolesMap = new MapSqlParameterSource()
                        .addValue("userId", newKey)
                        .addValue("role", role);
                insertRole.execute(rolesMap);
            });
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map);
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                List<String> roles = user.getRoles().stream().map(Enum::toString).collect(Collectors.toList());

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, user.getId());
                    ps.setString(2, roles.get(i));
                }

                @Override
                public int getBatchSize() {
                    return roles.size();
                }
            });
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        User user = DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id));
        if (user != null) setRoles(user);
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email));
        if (user != null) setRoles(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        if (!users.isEmpty()) users.forEach(this::setRoles);
        return users;
    }

    private void setRoles(User user) {
        List<String> list = namedParameterJdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=:userId", new MapSqlParameterSource("userId", user.getId()), String.class);
        user.setRoles(list.stream().map(Role::valueOf).collect(Collectors.toSet()));
    }
}