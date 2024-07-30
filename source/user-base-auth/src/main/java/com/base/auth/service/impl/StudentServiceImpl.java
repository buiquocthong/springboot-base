package com.base.auth.service.impl;

import com.base.auth.model.Student;
import com.base.auth.repository.StudentRepository;
import com.base.auth.service.StudentService;

import java.util.Optional;

public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    @Override
    public Optional<Student> getStudentByAddress(String address) {
        return studentRepository.findByAddress(address);
    }

    @Override
    public Optional<Student> getStudentByIdentification(String id) {
        return studentRepository.findByIdentification(id);
    }
}
