package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.FoodItem;
import com.sku.caloriechat.domain.Meal;
import java.sql.*;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MealDao {

    private final DataSource dataSource;

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
}