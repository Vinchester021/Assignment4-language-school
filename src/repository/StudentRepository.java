package repository;

import exception.DatabaseOperationException;
import model.Student;
import repository.interfaces.CrudRepository;
import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StudentRepository implements CrudRepository<Student> {

    // CREATE
    public Student create(Student s) {
        String insertPerson =
                "INSERT INTO persons(name,email,role) VALUES (?,?, 'STUDENT') RETURNING id";
        String insertStudent =
                "INSERT INTO students(person_id, level, discount_percent) VALUES (?,?,?)";

        Connection con = DatabaseConnection.getConnection();

        try {
            con.setAutoCommit(false);
            int personId;

            try (PreparedStatement ps1 = con.prepareStatement(insertPerson)) {
                ps1.setString(1, s.getName());
                ps1.setString(2, s.getEmail());

                try (ResultSet rs = ps1.executeQuery()) {
                    if (!rs.next()) {
                        throw new DatabaseOperationException("Failed to insert person (no id returned)");
                    }
                    personId = rs.getInt("id");
                }
            }

            try (PreparedStatement ps2 = con.prepareStatement(insertStudent)) {
                ps2.setInt(1, personId);
                ps2.setString(2, s.getLevel());
                ps2.setInt(3, s.getDiscountPercent());
                ps2.executeUpdate();
            }

            con.commit();

            return new Student(
                    personId,
                    s.getName(),
                    s.getEmail(),
                    s.getLevel(),
                    s.getDiscountPercent()
            );

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignore) {}
            throw new DatabaseOperationException("Failed to create student", e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // READ ALL
    public List<Student> getAll() {
        String sql = """
                SELECT p.id, p.name, p.email, s.level, s.discount_percent
                FROM persons p
                JOIN students s ON s.person_id = p.id
                ORDER BY p.id
                """;

        List<Student> list = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("level"),
                        rs.getInt("discount_percent")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get students", e);
        }
    }

    // READ BY ID
    public Student getById(int id) {
        String sql = """
                SELECT p.id, p.name, p.email, s.level, s.discount_percent
                FROM persons p
                JOIN students s ON s.person_id = p.id
                WHERE p.id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("level"),
                        rs.getInt("discount_percent")
                );
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get student by id", e);
        }
    }

    // UPDATE
    public void update(int id, Student s) {
        String updatePerson =
                "UPDATE persons SET name=?, email=? WHERE id=? AND role='STUDENT'";
        String updateStudent =
                "UPDATE students SET level=?, discount_percent=? WHERE person_id=?";

        Connection con = DatabaseConnection.getConnection();

        try {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(updatePerson)) {
                ps1.setString(1, s.getName());
                ps1.setString(2, s.getEmail());
                ps1.setInt(3, id);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = con.prepareStatement(updateStudent)) {
                ps2.setString(1, s.getLevel());
                ps2.setInt(2, s.getDiscountPercent());
                ps2.setInt(3, id);
                ps2.executeUpdate();
            }

            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignore) {}
            throw new DatabaseOperationException("Failed to update student", e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM persons WHERE id=? AND role='STUDENT'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete student", e);
        }
    }
}
