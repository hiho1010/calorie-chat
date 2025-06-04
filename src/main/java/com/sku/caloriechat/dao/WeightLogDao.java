package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.WeightLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WeightLogDao {

    private final JdbcTemplate jdbc;

    public void save(WeightLog log) {
        String sql = """
            INSERT INTO weight_log (user_id, date, weight, created_at)
            VALUES (?, ?, ?, NOW())
        """;

        jdbc.update(sql, log.getUserId(), log.getDate(), log.getWeight());
    }

    public List<WeightLog> findByUserId(Long userId) {
        String sql = """
            SELECT * FROM weight_log
            WHERE user_id = ? AND deleted_at IS NULL
            ORDER BY date
        """;

        return jdbc.query(sql, weightLogRowMapper, userId);
    }

    private final RowMapper<WeightLog> weightLogRowMapper = (rs, rowNum) -> {
        Timestamp deleted = rs.getTimestamp("deleted_at");

        return new WeightLog(
                rs.getLong("weight_log_id"),
                rs.getLong("user_id"),
                rs.getDate("date").toLocalDate(),
                rs.getFloat("weight"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                deleted == null ? null : deleted.toLocalDateTime()
        );
    };
}