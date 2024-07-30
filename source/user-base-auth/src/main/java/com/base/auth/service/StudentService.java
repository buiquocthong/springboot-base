package com.base.auth.service;

import com.base.auth.model.Student;
import com.base.auth.repository.StudentRepository;

import java.util.Optional;

public interface StudentService {
    Optional<Student>getStudentByAddress(String address);
    Optional<Student>getStudentByIdentification(String address);

}
