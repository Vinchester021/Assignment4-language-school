package utils;

import model.Student;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {
    public static List<Student> sortStudentsByName(List<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }
}
