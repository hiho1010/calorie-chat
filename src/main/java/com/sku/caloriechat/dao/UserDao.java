package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbc;

    public long save(User u) {
        String sql = """
            INSERT INTO `user`
            (status, user_name, email, password, gender, age, height, weight,
             activity_level, goal_weight, target_loss_speed, created_at)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,NOW())
            """;

        KeyHolder kh = new GeneratedKeyHolder();

        jdbc.update(conn -> {
            PreparedStatement ps =
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString   (1,  u.getStatus().name()); // enum → String
            ps.setString   (2,  u.getUserName());
            ps.setString   (3,  u.getEmail());
            ps.setString   (4,  u.getPassword());
            ps.setString   (5,  u.getGender());
            ps.setInt      (6,  u.getAge());
            ps.setBigDecimal(7, u.getHeight());
            ps.setBigDecimal(8, u.getWeight());
            ps.setString   (9,  u.getActivityLevel());
            ps.setBigDecimal(10, u.getGoalWeight());
            ps.setString   (11, u.getTargetLossSpeed());
            return ps;
        }, kh);

        return kh.getKey() == null ? -1L : kh.getKey().longValue();
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
            SELECT * FROM `user`
            WHERE email = ? AND deleted_at IS NULL
            """;
        return jdbc.query(sql, userRowMapper, email).stream().findFirst();
    }

    public boolean existsByEmail(String email) {
        Integer cnt = jdbc.queryForObject(
            "SELECT COUNT(*) FROM `user` WHERE email = ? AND deleted_at IS NULL",
            Integer.class,
            email
        );
        return cnt != null && cnt > 0;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        Timestamp updated = rs.getTimestamp("updated_at");
        Timestamp deleted = rs.getTimestamp("deleted_at");
        return new User(
            rs.getLong("user_id"),
            UserStatus.valueOf(rs.getString("status")), // String → enum
            rs.getString("user_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("gender"),
            rs.getInt("age"),
            rs.getBigDecimal("height"),
            rs.getBigDecimal("weight"),
            rs.getString("activity_level"),
            rs.getBigDecimal("goal_weight"),
            rs.getString("target_loss_speed"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            updated == null ? null : updated.toLocalDateTime(),
            deleted == null ? null : deleted.toLocalDateTime()
        );
    };
}