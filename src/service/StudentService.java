package service;

import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;
import model.Student;
import repository.StudentRepository;

import java.sql.SQLIntegrityConstraintViolationException;

public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public Student create(Student s) {
        s.validate();

        for (Student existing : repo.getAll()) {
            if (existing.getEmail().equalsIgnoreCase(s.getEmail())) {
                throw new DuplicateResourceException("Student email already exists: " + s.getEmail());
            }
        }

        return repo.create(s);
    }

    public Student getById(int id) {
        Student s = repo.getById(id);
        if (s == null) throw new ResourceNotFoundException("Student not found: id=" + id);
        return s;
    }

    public void update(int id, Student s) {
        s.validate();
        getById(id);
        repo.update(id, s);
    }

    public void delete(int id) {
        getById(id);
        repo.delete(id);
    }
}

