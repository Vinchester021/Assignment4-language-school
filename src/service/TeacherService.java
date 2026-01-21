package service;

import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;
import model.Teacher;
import repository.TeacherRepository;

public class TeacherService {
    private final TeacherRepository repo;

    public TeacherService(TeacherRepository repo) {
        this.repo = repo;
    }

    public Teacher create(Teacher t) {
        t.validate();

        for (Teacher existing : repo.getAll()) {
            if (existing.getEmail().equalsIgnoreCase(t.getEmail())) {
                throw new DuplicateResourceException("Teacher email already exists: " + t.getEmail());
            }
        }
        return repo.create(t);
    }

    public Teacher getById(int id) {
        Teacher t = repo.getById(id);
        if (t == null) throw new ResourceNotFoundException("Teacher not found: id=" + id);
        return t;
    }

    public void update(int id, Teacher t) {
        t.validate();
        getById(id);
        repo.update(id, t);
    }

    public void delete(int id) {
        getById(id);
        repo.delete(id);
    }
}
