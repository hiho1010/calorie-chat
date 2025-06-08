package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.enums.ActivityLevel;
import com.sku.caloriechat.enums.TargetLossSpeed;
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

    /** 최초 회원가입 – 이메일·비밀번호·상태만 저장 */
    public long saveMinimal(String email, String hashedPw) {
        String sql = """
            INSERT INTO `user` (status, email, password, created_at)
            VALUES (?, ?, ?, NOW())
            """;

        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps =
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, UserStatus.ACTIVE.name());
            ps.setString(2, email);
            ps.setString(3, hashedPw);
            return ps;
        }, kh);
        return kh.getKey() == null ? -1L : kh.getKey().longValue();
    }

    /** 프로필 정보(닉네임·신체치수 등) 부분 업데이트 – null 값은 무시 */
    public void updateProfile(Long id, User profile) {
        String sql = """
            UPDATE `user` SET
              user_name         = COALESCE(?, user_name),
              gender            = COALESCE(?, gender),
              age               = COALESCE(?, age),
              height            = COALESCE(?, height),
              weight            = COALESCE(?, weight),
              activity_level    = COALESCE(?, activity_level),
              goal_weight       = COALESCE(?, goal_weight),
              target_loss_speed = COALESCE(?, target_loss_speed),
              updated_at        = NOW()
            WHERE user_id = ? AND deleted_at IS NULL
            """;
        jdbc.update(sql,
            profile.getUserName(),
            profile.getGender(),
            profile.getAge(),
            profile.getHeight(),
            profile.getWeight(),
            profile.getActivityLevel(),
            profile.getGoalWeight(),
            profile.getTargetLossSpeed(),
            id
        );
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
            UserStatus.valueOf(rs.getString("status")),
            rs.getString("user_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("gender"),
            rs.getInt("age"),
            rs.getBigDecimal("height"),
            rs.getBigDecimal("weight"),
            ActivityLevel.valueOf(rs.getString("activity_level")),
            rs.getBigDecimal("goal_weight"),
            TargetLossSpeed.valueOf(rs.getString("target_loss_speed")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            updated == null ? null : updated.toLocalDateTime(),
            deleted == null ? null : deleted.toLocalDateTime()
        );
    };

    public Optional<User> findById(Long id) {
        String sql = """
        SELECT * FROM `user`
        WHERE user_id = ? AND deleted_at IS NULL
    """;
        return jdbc.query(sql, userRowMapper, id).stream().findFirst();
    }
}