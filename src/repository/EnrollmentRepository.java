package repository;

import exception.DatabaseOperationException;
import model.Enrollment;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepository {
    private final DatabaseConnection db;

    public EnrollmentRepository(DatabaseConnection db) {
        this.db = db;
    }

    public Enrollment create(Enrollment e) {
        String sql = "INSERT INTO enrollments(student_id, course_id, enrolled_at) VALUES (?,?,?) RETURNING id";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, e.getStudent().getId());
            ps.setInt(2, e.getCourse().getId());
            ps.setDate(3, Date.valueOf(e.getEnrolledAt()));

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int id = rs.getInt("id");
                return new Enrollment(id, e.getStudent(), e.getCourse(), e.getEnrolledAt());
            }

        } catch (SQLException ex) {
            throw new DatabaseOperationException("Failed to create enrollment", ex);
        }
    }

    public List<Enrollment> getAll(StudentRepository studentRepo, CourseRepository courseRepo) {
        String sql = "SELECT id, student_id, course_id, enrolled_at FROM enrollments ORDER BY id";
        List<Enrollment> list = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                int courseId = rs.getInt("course_id");
                LocalDate date = rs.getDate("enrolled_at").toLocalDate();

                var student = studentRepo.getById(studentId);
                var course = courseRepo.getById(courseId);

                // если БД целостная, null быть не должно, но на всякий случай
                if (student != null && course != null) {
                    list.add(new Enrollment(id, student, course, date));
                }
            }
            return list;

        } catch (SQLException ex) {
            throw new DatabaseOperationException("Failed to get enrollments", ex);
        }
    }

    public Enrollment getById(int id, StudentRepository studentRepo, CourseRepository courseRepo) {
        String sql = "SELECT id, student_id, course_id, enrolled_at FROM enrollments WHERE id=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                int studentId = rs.getInt("student_id");
                int courseId = rs.getInt("course_id");
                LocalDate date = rs.getDate("enrolled_at").toLocalDate();

                var student = studentRepo.getById(studentId);
                var course = courseRepo.getById(courseId);

                if (student == null || course == null) return null;
                return new Enrollment(id, student, course, date);
            }

        } catch (SQLException ex) {
            throw new DatabaseOperationException("Failed to get enrollment by id", ex);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM enrollments WHERE id=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseOperationException("Failed to delete enrollment", ex);
        }
    }
}
