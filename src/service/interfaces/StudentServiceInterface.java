package service.interfaces;

import model.Student;
import java.util.List;

public interface StudentServiceInterface {

    Student create(Student student);
    Student getById(int id);
    List<Student> getAll();

    void update(int id, Student student);
    void delete(int id);
}
