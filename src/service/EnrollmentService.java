package service;

import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;
import model.Course;
import model.Enrollment;
import model.Student;
import repository.CourseRepository;
import repository.EnrollmentRepository;
import repository.StudentRepository;

import java.time.LocalDate;

public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepo;
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;

    public EnrollmentService(EnrollmentRepository enrollmentRepo, StudentRepository studentRepo, CourseRepository courseRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    public Enrollment enroll(int studentId, int courseId) {
        // FK checks
        Student student = studentRepo.getById(studentId);
        if (student == null) throw new ResourceNotFoundException("Student not found: id=" + studentId);

        Course course = courseRepo.getById(courseId);
        if (course == null) throw new ResourceNotFoundException("Course not found: id=" + courseId);

        // Duplicate rule: student_id + course_id unique
        for (Enrollment e : enrollmentRepo.getAll(studentRepo, courseRepo)) {
            if (e.getStudent().getId() == studentId && e.getCourse().getId() == courseId) {
                throw new DuplicateResourceException("Student already enrolled to this course: studentId="
                        + studentId + ", courseId=" + courseId);
            }
        }

        Enrollment enrollment = new Enrollment(0, student, course, LocalDate.now());
        enrollment.validate();

        return enrollmentRepo.create(enrollment);
    }

    public void delete(int enrollmentId) {
        Enrollment existing = enrollmentRepo.getById(enrollmentId, studentRepo, courseRepo);
        if (existing == null) throw new ResourceNotFoundException("Enrollment not found: id=" + enrollmentId);
        enrollmentRepo.delete(enrollmentId);
    }
}
