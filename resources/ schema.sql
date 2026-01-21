DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS persons;

--Student/Teacher
CREATE TABLE persons (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL CHECK (length(trim(name)) > 0),
                         email VARCHAR(150) NOT NULL UNIQUE,
                         role VARCHAR(20) NOT NULL CHECK (role IN ('STUDENT', 'TEACHER'))
);

CREATE TABLE students (
                          person_id INT PRIMARY KEY,
                          level VARCHAR(10) NOT NULL CHECK (length(trim(level)) > 0),
                          discount_percent INT NOT NULL DEFAULT 0 CHECK (discount_percent >= 0 AND discount_percent <= 100),
                          CONSTRAINT fk_students_person FOREIGN KEY (person_id)
                              REFERENCES persons(id) ON DELETE CASCADE
);

CREATE TABLE teachers (
                          person_id INT PRIMARY KEY,
                          specialization VARCHAR(50) NOT NULL CHECK (length(trim(specialization)) > 0),
                          salary_per_month NUMERIC(12,2) NOT NULL CHECK (salary_per_month > 0),
                          CONSTRAINT fk_teachers_person FOREIGN KEY (person_id)
                              REFERENCES persons(id) ON DELETE CASCADE
);

--courses(FK)
CREATE TABLE courses (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(120) NOT NULL CHECK (length(trim(name)) > 0),
                         level VARCHAR(10) NOT NULL CHECK (length(trim(level)) > 0),
                         price NUMERIC(12,2) NOT NULL CHECK (price > 0),
                         teacher_id INT NOT NULL,
                         CONSTRAINT fk_courses_teacher FOREIGN KEY (teacher_id)
                             REFERENCES teachers(person_id) ON DELETE RESTRICT,
                         CONSTRAINT uq_course_name_level UNIQUE (name, level)
);

CREATE TABLE enrollments (
                             id SERIAL PRIMARY KEY,
                             student_id INT NOT NULL,
                             course_id INT NOT NULL,
                             enrolled_at DATE NOT NULL DEFAULT CURRENT_DATE,
                             CONSTRAINT fk_enroll_student FOREIGN KEY (student_id)
                                 REFERENCES students(person_id) ON DELETE CASCADE,
                             CONSTRAINT fk_enroll_course FOREIGN KEY (course_id)
                                 REFERENCES courses(id) ON DELETE CASCADE,
                             CONSTRAINT uq_enrollment UNIQUE (student_id, course_id)
);

--Teacher
INSERT INTO persons(name,email,role) VALUES ('Igor', 'igor@mail.ru', 'TEACHER');
INSERT INTO teachers(person_id, specialization, salary_per_month)
VALUES (currval('persons_id_seq'), 'IELTS', 350000);

--Student
INSERT INTO persons(name,email,role) VALUES ('Artem', 'artem@mail.ru', 'STUDENT');
INSERT INTO students(person_id, level, discount_percent)
VALUES (currval('persons_id_seq'), 'A2', 10);

--Course
INSERT INTO courses(name, level, price, teacher_id)
VALUES ('General English', 'A2', 60000, (SELECT person_id FROM teachers LIMIT 1));

--Enrollment
INSERT INTO enrollments(student_id, course_id, enrolled_at)
VALUES (
           (SELECT person_id FROM students LIMIT 1),
       (SELECT id FROM courses LIMIT 1),
            CURRENT_DATE
    );
