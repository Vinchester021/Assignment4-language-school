package service;

import model.Student;
import repository.StudentRepository;
import service.interfaces.StudentServiceInterface;
import exception.ResourceNotFoundException;
import java.util.List;
import java.util.Comparator;
import java.util.List;

public class StudentService implements StudentServiceInterface {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    @Override
    public Student create(Student student) {
        student.validate();
        return repo.create(student);
    }

    @Override
    public Student getById(int id) {
        Student s = repo.getById(id);
        if (s == null) {
            throw new ResourceNotFoundException("Student not found: id=" + id);
        }
        return s;
    }

    @Override
    public List<Student> getAll() {
        return repo.getAll();
    }

    @Override
    public void update(int id, Student student) {
        student.validate();
        repo.update(id, student);
    }

    @Override
    public void delete(int id) {
        repo.delete(id);
    }

    public List<Student> getAllSortedByName() {
        return repo.getAll().stream()
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }
}

