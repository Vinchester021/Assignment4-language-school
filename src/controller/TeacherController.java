package controller;

import model.Teacher;
import service.TeacherService;

public class TeacherController {
    private final TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }

    public Teacher create(Teacher t) {
        Teacher created = service.create(t);
        System.out.println("{\"status\":\"OK\",\"action\":\"createTeacher\",\"id\":" + created.getId() + "}");
        return created;
    }
}
