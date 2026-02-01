import controller.CourseController;
import controller.EnrollmentController;
import controller.StudentController;
import controller.TeacherController;
import db.DatabaseConnection;
import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Course;
import model.Student;
import model.Teacher;
import repository.CourseRepository;
import repository.EnrollmentRepository;
import repository.StudentRepository;
import repository.TeacherRepository;
import service.CourseService;
import service.EnrollmentService;
import service.StudentService;
import service.TeacherService;
import utils.ConsoleFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.ReflectionUtils;
import utils.SortingUtils;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.getConnection();
        printTables();

        // STUDENT
        ConsoleFormatter.title("        Student operations");
        StudentRepository studentRepo = new StudentRepository();
        StudentService studentService = new StudentService(studentRepo);
        StudentController studentController = new StudentController(studentService);
        Student created = studentController.create(
                new Student(0, "Petr", "petr_318@mail.ru", "A2", 10)
        );
        ConsoleFormatter.success("Created student: " + created.shortInfo());

        try {
            studentController.create(
                    new Student(0, "", "bad@mail.ru", "A1", 0)
            );
        } catch (InvalidInputException e) {
            ConsoleFormatter.error("INVALID_INPUT: " + e.getMessage());
        }

        try {
            studentController.getById(999);
        } catch (ResourceNotFoundException e) {
            ConsoleFormatter.error("NOT_FOUND: " + e.getMessage());
        }

        ConsoleFormatter.separator();
        ConsoleFormatter.title("Students sorted by name (Lambda/Stream demo)");

        SortingUtils.sortStudentsByName(studentService.getAll())
                .forEach(s -> System.out.println("[SORTED] " + s.shortInfo()));

        // TEACHER
        ConsoleFormatter.title("        Teacher operations");
        TeacherRepository teacherRepo = new TeacherRepository();
        TeacherService teacherService = new TeacherService(teacherRepo);
        TeacherController teacherController = new TeacherController(teacherService);
        Teacher t1 = teacherController.create(
                new Teacher(0, "Timur", "timur_19@mail.ru", "IELTS", 350000)
        );
        ConsoleFormatter.success("Created teacher: " + t1.shortInfo());
        ConsoleFormatter.separator();

        // COURSE
        ConsoleFormatter.title("        Course operations");
        CourseRepository courseRepo = new CourseRepository();
        CourseService courseService = new CourseService(courseRepo, teacherRepo);
        CourseController courseController = new CourseController(courseService);

        Course c1 = courseController.create(
                new Course(0, "General English", "A2", 45000, t1.getId())
        );
        ConsoleFormatter.success(
                "Created course: " + c1.getName()
                        + " " + c1.getLevel()
                        + " (teacherId = " + c1.getTeacherId() + ")"
        );
        ConsoleFormatter.separator();

        // ENROLLMENT
        ConsoleFormatter.title("        Enrollment");
        EnrollmentRepository enrollmentRepo = new EnrollmentRepository();
        EnrollmentService enrollmentService =
                new EnrollmentService(enrollmentRepo, studentRepo, courseRepo);
        EnrollmentController enrollmentController =
                new EnrollmentController(enrollmentService);

        var e1 = enrollmentController.enroll(created.getId(), c1.getId());
        ConsoleFormatter.success(
                "Enrolled: enrollmentId=" + e1.getId()
                        + " studentId=" + created.getId()
                        + " courseId=" + c1.getId()
        );

        try {
            enrollmentController.enroll(created.getId(), c1.getId());
        } catch (DuplicateResourceException ex) {
            ConsoleFormatter.error("DUPLICATE: " + ex.getMessage());
        }
        ConsoleFormatter.separator();
        ConsoleFormatter.title("        Reflection demo");

        ReflectionUtils.printClassInfo(Student.class);
        ReflectionUtils.printClassInfo(Teacher.class);
        ConsoleFormatter.separator();
        ConsoleFormatter.info("DONE");
    }

    // JDBC smoke test
    private static void printTables() {
        String sql =
                "SELECT table_name FROM information_schema.tables " +
                        "WHERE table_schema = 'public'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Tables in DB:");
            System.out.println("--------------------------------");
            while (rs.next()) {
                System.out.println(" < " + rs.getString("table_name") + " > ");
            }
            System.out.println("--------------------------------");

        } catch (Exception e) {
            System.out.println("Smoke test failed: " + e.getMessage());
        }
    }
}
