package com.base.auth.mapper;

import com.base.auth.dto.student.StudentAutoCompleteDto;
import com.base.auth.dto.student.StudentDto;
import com.base.auth.model.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AccountMapper.class})
public interface StudentMapper {

    @Mapping(source = "id",target = "id")
    @Mapping(source = "birthday",target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(source ="account",target = "account",qualifiedByName="fromAccountToDto")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "identification", target = "identification")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromStudentToStudentDto")
    StudentDto fromEntityToStudentDto(Student student);

    @Mapping(source = "id",target = "id")
    @Mapping(source ="account",target = "accountAutoCompleteDto",qualifiedByName="fromAccountToAutoCompleteDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromStudentToStudentDtoAutoComplete")
    StudentAutoCompleteDto fromStudentToDtoAutoComplete(Student student);




    @IterableMapping(elementTargetType = StudentDto.class,qualifiedByName = "fromStudentToStudentDto")
    @BeanMapping(ignoreByDefault = true)
    List<StudentDto> fromStudentListToStudentDtoList(List<Student> list);

    @IterableMapping(elementTargetType = StudentAutoCompleteDto.class,qualifiedByName = "fromStudentToStudentDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    List<StudentAutoCompleteDto> fromStudentListToStudentDtoListAutocomplete(List<Student> list);
}