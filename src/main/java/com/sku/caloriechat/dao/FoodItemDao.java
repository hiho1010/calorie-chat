package com.sku.caloriechat.dao;

import com.sku.caloriechat.domain.FoodItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FoodItemDao {

    private final DataSource dataSource;

    public void batchInsert(List<FoodItem> items) throws SQLException {
        if (items.isEmpty()) return;

        String sql =
            "INSERT INTO food_item (meal_id, name, calories, quantity, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            for (FoodItem fi : items) {
                ps.setInt(1, fi.getMealId());
                ps.setString(2, fi.getName());
                ps.setFloat(3, fi.getCalories());
                ps.setString(4, fi.getQuantity());                 // ✅
                ps.setTimestamp(5, Timestamp.valueOf(fi.getCreatedAt()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public List<FoodItem> findByMealId(int mealId) throws SQLException {
        String sql = "SELECT * FROM food_item WHERE meal_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mealId);
            try (ResultSet rs = ps.executeQuery()) {
                List<FoodItem> items = new ArrayList<>();
                while (rs.next()) {
                    FoodItem item = new FoodItem();
                    item.setFoodItemId(rs.getInt("food_item_id"));
                    item.setMealId(mealId);
                    item.setName(rs.getString("name"));
                    item.setCalories(rs.getFloat("calories"));
                    item.setQuantity(rs.getString("quantity"));
                    item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    items.add(item);
                }
                return items;
            }
        }
    }

}