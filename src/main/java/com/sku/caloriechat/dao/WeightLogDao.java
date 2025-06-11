package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.WeightLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WeightLogDao {

    private final JdbcTemplate jdbc;

    // 몸무게 저장
    public void save(WeightLog log) {
        String sql = """
            INSERT INTO weight_log (user_id, date, weight, created_at)
            VALUES (?, ?, ?, NOW())
        """;

        jdbc.update(sql, log.getUserId(), log.getDate(), log.getWeight());
    }

    // 특정 유저의 모든 몸무게 기록 조회
    public List<WeightLog> findByUserId(Long userId) {
        String sql = """
            SELECT * FROM weight_log
            WHERE user_id = ? AND deleted_at IS NULL
            ORDER BY date
        """;

        return jdbc.query(sql, weightLogRowMapper, userId);
    }

    // ✅ 가장 최근 몸무게 기록 1건 조회 (칼로리 계산용)
    public Optional<WeightLog> findLatestByUserId(Long userId) {
        String sql = """
            SELECT * FROM weight_log
            WHERE user_id = ? AND deleted_at IS NULL
            ORDER BY date DESC, created_at DESC
            LIMIT 1
        """;

        List<WeightLog> result = jdbc.query(sql, weightLogRowMapper, userId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    // 공통 RowMapper
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
