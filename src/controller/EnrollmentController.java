package controller;

import model.Enrollment;
import service.EnrollmentService;

public class EnrollmentController {
    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    public Enrollment enroll(int studentId, int courseId) {
        Enrollment e = service.enroll(studentId, courseId);
        System.out.println("{\"status\":\"OK\",\"action\":\"enroll\",\"id\":" + e.getId()
                + ",\"studentId\":" + studentId + ",\"courseId\":" + courseId + "}");
        return e;
    }

    public void delete(int enrollmentId) {
        service.delete(enrollmentId);
        System.out.println("{\"status\":\"OK\",\"action\":\"deleteEnrollment\",\"id\":" + enrollmentId + "}");
    }
}
