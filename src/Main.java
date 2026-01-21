import controller.CourseController;
import controller.EnrollmentController;
import controller.StudentController;
import controller.TeacherController;
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
import utils.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/LanguageSchoolDB";
        String user = "postgres";
        String pass = "GGtemir099!";

        DatabaseConnection db = new DatabaseConnection(url, user, pass);

        //STUDENT
        ConsoleFormatter.title("        Student operations");

        StudentRepository repo = new StudentRepository(db);
        StudentService service = new StudentService(repo);
        StudentController controller = new StudentController(service);

        Student created = controller.create(
                new Student(0, "Artem", "artem_312@mail.ru", "A2", 10)
        );
        ConsoleFormatter.success("Created student: " + created.shortInfo());

        try {
            controller.create(new Student(0, "Artem2", "artem_305@mail.ru", "A1", 0));
        } catch (DuplicateResourceException e) {
            ConsoleFormatter.error("DUPLICATE: " + e.getMessage());
        }

        try {
            controller.create(new Student(0, "", "bad@mail.ru", "A1", 0));
        } catch (InvalidInputException e) {
            ConsoleFormatter.error("INVALID_INPUT: " + e.getMessage());
        }

        try {
            controller.getById(999999);
        } catch (ResourceNotFoundException e) {
            ConsoleFormatter.error("NOT_FOUND: " + e.getMessage());
        }

        ConsoleFormatter.separator();

        //TEACHER
        ConsoleFormatter.title("        Teacher operations");

        TeacherRepository teacherRepo = new TeacherRepository(db);
        TeacherService teacherService = new TeacherService(teacherRepo);
        TeacherController teacherController = new TeacherController(teacherService);

        Teacher t1 = teacherController.create(
                new Teacher(0, "Igor", "igor_9@mail.ru", "IELTS", 350000)
        );
        ConsoleFormatter.success("Created teacher: " + t1.shortInfo());

        ConsoleFormatter.separator();

        //COURSE
        ConsoleFormatter.title("        Course operations");

        CourseRepository courseRepo = new CourseRepository(db);
        CourseService courseService = new CourseService(courseRepo, teacherRepo);
        CourseController courseController = new CourseController(courseService);

        Course c1 = courseController.create(
                new Course(0, "General English", "C1", 60000, t1.getId())
        );
        ConsoleFormatter.success("Created course: " + c1.getName() + " " + c1.getLevel()
                + " (teacherId = " + c1.getTeacherId() + ")");

        ConsoleFormatter.separator();

        //ENROLLMENT
        ConsoleFormatter.title("        Enrollment");

        EnrollmentRepository enrollmentRepo = new EnrollmentRepository(db);
        EnrollmentService enrollmentService = new EnrollmentService(enrollmentRepo, repo, courseRepo);
        EnrollmentController enrollmentController = new EnrollmentController(enrollmentService);

        var e1 = enrollmentController.enroll(created.getId(), c1.getId());
        ConsoleFormatter.success("Enrolled: enrollmentId = " + e1.getId()
                + " studentId = " + created.getId()
                + " courseId = " + c1.getId());

        try {
            enrollmentController.enroll(created.getId(), c1.getId());
        } catch (DuplicateResourceException ex) {
            ConsoleFormatter.error("DUPLICATE: " + ex.getMessage());
        }

        ConsoleFormatter.separator();
        ConsoleFormatter.info("DONE");
    }
}
