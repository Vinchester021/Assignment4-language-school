package repository;

import db.DatabaseConnection;
import exception.DatabaseOperationException;
import model.Teacher;
import repository.interfaces.CrudRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository implements CrudRepository<Teacher> {

    // CREATE
    public Teacher create(Teacher t) {
        String insertPerson = "INSERT INTO persons(name,email,role) VALUES (?,?, 'TEACHER') RETURNING id";
        String insertTeacher = "INSERT INTO teachers(person_id, specialization, salary_per_month) VALUES (?,?,?)";

        Connection con = DatabaseConnection.getConnection();

        try {
            con.setAutoCommit(false);

            int personId;
            try (PreparedStatement ps1 = con.prepareStatement(insertPerson)) {
                ps1.setString(1, t.getName());
                ps1.setString(2, t.getEmail());

                try (ResultSet rs = ps1.executeQuery()) {
                    if (!rs.next()) {
                        throw new DatabaseOperationException("Failed to insert person (no id returned)");
                    }
                    personId = rs.getInt("id");
                }
            }

            try (PreparedStatement ps2 = con.prepareStatement(insertTeacher)) {
                ps2.setInt(1, personId);
                ps2.setString(2, t.getSpecialization());
                ps2.setDouble(3, t.getSalaryPerMonth());
                ps2.executeUpdate();
            }

            con.commit();

            return new Teacher(
                    personId,
                    t.getName(),
                    t.getEmail(),
                    t.getSpecialization(),
                    t.getSalaryPerMonth()
            );

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignore) {}
            throw new DatabaseOperationException("Failed to create teacher", e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // READ ALL
    public List<Teacher> getAll() {
        String sql = """
            SELECT p.id, p.name, p.email, t.specialization, t.salary_per_month
            FROM persons p
            JOIN teachers t ON t.person_id = p.id
            ORDER BY p.id
        """;

        List<Teacher> list = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("specialization"),
                        rs.getDouble("salary_per_month")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get teachers", e);
        }
    }

    // READ BY ID
    public Teacher getById(int id) {
        String sql = """
            SELECT p.id, p.name, p.email, t.specialization, t.salary_per_month
            FROM persons p
            JOIN teachers t ON t.person_id = p.id
            WHERE p.id = ?
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new Teacher(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("specialization"),
                        rs.getDouble("salary_per_month")
                );
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get teacher by id", e);
        }
    }

    // UPDATE
    public void update(int id, Teacher t) {
        String updatePerson = "UPDATE persons SET name=?, email=? WHERE id=? AND role='TEACHER'";
        String updateTeacher = "UPDATE teachers SET specialization=?, salary_per_month=? WHERE person_id=?";

        Connection con = DatabaseConnection.getConnection();

        try {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(updatePerson)) {
                ps1.setString(1, t.getName());
                ps1.setString(2, t.getEmail());
                ps1.setInt(3, id);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = con.prepareStatement(updateTeacher)) {
                ps2.setString(1, t.getSpecialization());
                ps2.setDouble(2, t.getSalaryPerMonth());
                ps2.setInt(3, id);
                ps2.executeUpdate();
            }

            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignore) {}
            throw new DatabaseOperationException("Failed to update teacher", e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM persons WHERE id=? AND role='TEACHER'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete teacher", e);
        }
    }
}
