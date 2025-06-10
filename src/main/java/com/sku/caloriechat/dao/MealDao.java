package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.FoodItem;
import com.sku.caloriechat.domain.Meal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MealDao {

    private final DataSource dataSource;

    private final JdbcTemplate jdbc;

    public int insert(Meal meal) throws SQLException {
        String sql =
            "INSERT INTO meal (user_id, meal_time, eaten_at, total_calories, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, meal.getUserId());
            ps.setString(2, meal.getMealTime());
            ps.setTimestamp(3, Timestamp.valueOf(meal.getEatenAt()));
            if (meal.getTotalCalories() == null) ps.setNull(4, Types.FLOAT);
            else ps.setFloat(4, meal.getTotalCalories());
            ps.setTimestamp(5, Timestamp.valueOf(meal.getCreatedAt()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);   // auto-increment meal_id
                }
            }
        }
        throw new SQLException("Meal insert 실패: PK를 얻을 수 없습니다.");
    }

    public Meal findById(int mealId) throws SQLException {
        String sql = "SELECT * FROM meal WHERE meal_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mealId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Meal meal = new Meal();
                    meal.setMealId(mealId);
                    meal.setUserId(rs.getInt("user_id"));
                    meal.setMealTime(rs.getString("meal_time"));
                    meal.setEatenAt(rs.getTimestamp("eaten_at").toLocalDateTime());
                    meal.setTotalCalories(rs.getFloat("total_calories"));
                    meal.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return meal;
                }
            }
        }
        throw new SQLException("해당 ID의 식단을 찾을 수 없습니다.");
    }



    public Optional<Meal> findLatestByUserIdAndDate(int userId, LocalDate date) {
        String sql = """
        SELECT * FROM meal
        WHERE user_id = ? AND DATE(eaten_at) = ?
        ORDER BY eaten_at DESC
        LIMIT 1
    """;

        return jdbc.query(sql, mealRowMapper, userId, Date.valueOf(date))
                .stream().findFirst();
    }

    /** Meal 매핑 */
    private final RowMapper<Meal> mealRowMapper = (rs, rowNum) -> {
        Meal meal = new Meal();
        meal.setMealId(rs.getInt("meal_id"));
        meal.setUserId(rs.getInt("user_id"));
        meal.setMealTime(rs.getString("meal_time"));
        meal.setEatenAt(rs.getTimestamp("eaten_at").toLocalDateTime());
        meal.setTotalCalories(rs.getFloat("total_calories"));
        meal.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp updated = rs.getTimestamp("updated_at");
        Timestamp deleted = rs.getTimestamp("deleted_at");
        meal.setUpdatedAt(updated != null ? updated.toLocalDateTime() : null);
        meal.setDeletedAt(deleted != null ? deleted.toLocalDateTime() : null);
        return meal;
    };
}