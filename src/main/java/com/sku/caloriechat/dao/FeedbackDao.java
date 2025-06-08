package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.FeedbackLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FeedbackDao {

    private final JdbcTemplate jdbc;

    public long save(FeedbackLog log) {
        String sql = """
            INSERT INTO feedback_log (user_id, date, feedback, created_at)
            VALUES (?, ?, ?, NOW())
        """;

        return jdbc.update(sql,
                log.getUserId(),
                log.getDate(),
                log.getFeedback()
        );
    }

    public List<FeedbackLog> findByUserId(Long userId) {
        String sql = """
            SELECT * FROM feedback_log
            WHERE user_id = ?
            ORDER BY date DESC
        """;

        return jdbc.query(sql, feedbackLogRowMapper, userId);
    }

    public Optional<FeedbackLog> findByUserIdAndDate(Long userId, LocalDate date) {
        String sql = """
            SELECT * FROM feedback_log
            WHERE user_id = ? AND date = ?
        """;

        return jdbc.query(sql, feedbackLogRowMapper, userId, date)
                .stream().findFirst();
    }

    private final RowMapper<FeedbackLog> feedbackLogRowMapper = (rs, rowNum) -> {
        Timestamp createdAt = rs.getTimestamp("created_at");

        FeedbackLog log = new FeedbackLog();
        log.setId(rs.getLong("id"));
        log.setUserId(rs.getLong("user_id"));
        log.setDate(rs.getDate("date").toLocalDate());
        log.setFeedback(rs.getString("feedback"));
        log.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        return log;
    };
}
