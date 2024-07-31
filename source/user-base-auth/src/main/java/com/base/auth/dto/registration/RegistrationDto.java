package com.base.auth.dto.registration;

import com.base.auth.dto.course.CourseDto;
import com.base.auth.dto.student.StudentDto;
import com.base.auth.model.Course;
import com.base.auth.model.Student;
import lombok.Data;

@Data
public class RegistrationDto {

    private Long id;
    private StudentDto student;
    private CourseDto course;
    private Boolean isFinished;

}
