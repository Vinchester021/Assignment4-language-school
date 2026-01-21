package service;

import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;
import model.Course;
import repository.CourseRepository;
import repository.TeacherRepository;

public class CourseService {
    private final CourseRepository courseRepo;
    private final TeacherRepository teacherRepo;

    public CourseService(CourseRepository courseRepo, TeacherRepository teacherRepo) {
        this.courseRepo = courseRepo;
        this.teacherRepo = teacherRepo;
    }

    public Course create(Course c) {
        c.validate();

        // FK must exist (teacher)
        if (teacherRepo.getById(c.getTeacherId()) == null) {
            throw new ResourceNotFoundException("Teacher not found for course: teacherId=" + c.getTeacherId());
        }

        // logical duplicate: (name, level)
        for (Course existing : courseRepo.getAll()) {
            if (existing.getName().equalsIgnoreCase(c.getName())
                    && existing.getLevel().equalsIgnoreCase(c.getLevel())) {
                throw new DuplicateResourceException("Course already exists: " + c.getName() + " / " + c.getLevel());
            }
        }

        return courseRepo.create(c);
    }

    public Course getById(int id) {
        Course c = courseRepo.getById(id);
        if (c == null) throw new ResourceNotFoundException("Course not found: id=" + id);
        return c;
    }

    public void update(int id, Course c) {
        c.validate();

        if (teacherRepo.getById(c.getTeacherId()) == null) {
            throw new ResourceNotFoundException("Teacher not found for course: teacherId=" + c.getTeacherId());
        }

        getById(id);
        courseRepo.update(id, c);
    }

    public void delete(int id) {
        getById(id);
        courseRepo.delete(id);
    }
}
