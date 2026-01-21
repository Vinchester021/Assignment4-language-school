package controller;

import model.Student;
import service.StudentService;

public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    public Student create(Student s) {
        Student created = service.create(s);
        System.out.println("{\"status\":\"OK\",\"action\":\"createStudent\",\"id\":" + created.getId() + "}");
        return created;
    }

    public Student getById(int id) {
        Student s = service.getById(id);
        System.out.println("{\"status\":\"OK\",\"action\":\"getStudent\",\"id\":" + s.getId() + "}");
        return s;
    }

    public void update(int id, Student s) {
        service.update(id, s);
        System.out.println("{\"status\":\"OK\",\"action\":\"updateStudent\",\"id\":" + id + "}");
    }

    public void delete(int id) {
        service.delete(id);
        System.out.println("{\"status\":\"OK\",\"action\":\"deleteStudent\",\"id\":" + id + "}");
    }
}
