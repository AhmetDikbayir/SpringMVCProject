package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.repository.StudentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component()
public class StudentService implements IStudentService{

    private StudentRepository studentRepository;


    public List<Student> listAllStudents() {
        return null;
    }
}
