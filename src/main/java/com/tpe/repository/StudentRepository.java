package com.tpe.repository;

import com.tpe.domain.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentRepository implements IStudentRepository {
    public List<Student> getAllStudents(){
        return null;

    }
}
