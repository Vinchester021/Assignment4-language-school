package controller;

import model.Course;
import service.CourseService;

public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    public Course create(Course c) {
        Course created = service.create(c);
        System.out.println("{\"status\":\"OK\",\"action\":\"createCourse\",\"id\":" + created.getId() + "}");
        return created;
    }
}
