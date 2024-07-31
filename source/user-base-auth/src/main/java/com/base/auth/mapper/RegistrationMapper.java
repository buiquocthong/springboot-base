package com.base.auth.mapper;

import com.base.auth.dto.registration.RegistrationDto;
import com.base.auth.dto.student.StudentAutoCompleteDto;
import com.base.auth.dto.student.StudentDto;
import com.base.auth.form.course.UpdateCourseForm;
import com.base.auth.form.registration.RegistrationForm;
import com.base.auth.form.registration.UpdateRegistrationForm;
import com.base.auth.model.Course;
import com.base.auth.model.Registration;
import com.base.auth.model.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {StudentMapper.class, CourseMapper.class})
public interface RegistrationMapper {

    @Mapping(source = "id",target = "id")
    @Mapping(source = "student", target = "student", qualifiedByName = "fromStudentToStudentDto")
    @Mapping(source = "course", target = "course", qualifiedByName = "fromCourseToDto")
    @Mapping(source = "isFinished", target = "isFinished")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromRegistrationToDto")
    RegistrationDto fromRegistrationToDto(Registration registration);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "isFinished", target = "isFinished")
    @BeanMapping(ignoreByDefault = true)
    Registration fromFormDtoToEntity(RegistrationForm registrationForm);

    @IterableMapping(elementTargetType = RegistrationDto.class, qualifiedByName = "fromRegistrationToDto")
    @BeanMapping(ignoreByDefault = true)
    List<RegistrationDto> fromListToRegistrationDtoList(List<Registration> list);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "isFinished", target = "isFinished")
    void updateFromUpdateRegistrationForm(UpdateRegistrationForm updateRegistrationForm, @MappingTarget Registration registration);
}
