package repository;

import exception.DatabaseOperationException;
import model.Course;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
    private final DatabaseConnection db;

    public CourseRepository(DatabaseConnection db) {
        this.db = db;
    }

    public Course create(Course c) {
        String sql = "INSERT INTO courses(name, level, price, teacher_id) VALUES (?,?,?,?) RETURNING id";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getLevel());
            ps.setDouble(3, c.getPrice());
            ps.setInt(4, c.getTeacherId());

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int id = rs.getInt("id");
                return new Course(id, c.getName(), c.getLevel(), c.getPrice(), c.getTeacherId());
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create course", e);
        }
    }

    public List<Course> getAll() {
        String sql = "SELECT id, name, level, price, teacher_id FROM courses ORDER BY id";

        List<Course> list = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("level"),
                        rs.getDouble("price"),
                        rs.getInt("teacher_id")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get courses", e);
        }
    }

    public Course getById(int id) {
        String sql = "SELECT id, name, level, price, teacher_id FROM courses WHERE id=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("level"),
                        rs.getDouble("price"),
                        rs.getInt("teacher_id")
                );
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get course by id", e);
        }
    }

    public void update(int id, Course c) {
        String sql = "UPDATE courses SET name=?, level=?, price=?, teacher_id=? WHERE id=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getLevel());
            ps.setDouble(3, c.getPrice());
            ps.setInt(4, c.getTeacherId());
            ps.setInt(5, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update course", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM courses WHERE id=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete course", e);
        }
    }
}
