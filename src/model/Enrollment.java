package model;

import exception.InvalidInputException;
import java.time.LocalDate;

public class Enrollment implements Validatable, Identifiable {

    private int id;
    private Student student;
    private Course course;
    private LocalDate enrolledAt;

    public Enrollment(int id, Student student, Course course, LocalDate enrolledAt) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enrolledAt = enrolledAt;
        validate();
    }

    @Override
    public void validate() {
        if (student == null) throw new InvalidInputException("Student must not be null");
        if (course == null) throw new InvalidInputException("Course must not be null");
        if (enrolledAt == null) throw new InvalidInputException("EnrolledAt must not be null");
    }

    @Override public int getId() { return id; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDate getEnrolledAt() { return enrolledAt; }
}