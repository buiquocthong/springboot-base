package com.base.auth.mapper;

import com.base.auth.dto.student.StudentAutoCompleteDto;
import com.base.auth.dto.student.StudentDto;
import com.base.auth.model.Student;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-30T17:01:50+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.24 (Amazon.com Inc.)"
)
@Component
public class StudentMapperImpl implements StudentMapper {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public StudentDto fromEntityToStudentDto(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentDto studentDto = new StudentDto();

        studentDto.setBirthday( student.getBirthday() );
        studentDto.setAddress( student.getAddress() );
        studentDto.setIdentification( student.getIdentification() );
        studentDto.setId( student.getId() );
        studentDto.setAccount( accountMapper.fromAccountToDto( student.getAccount() ) );

        return studentDto;
    }

    @Override
    public StudentAutoCompleteDto fromStudentToDtoAutoComplete(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentAutoCompleteDto studentAutoCompleteDto = new StudentAutoCompleteDto();

        studentAutoCompleteDto.setAccountAutoCompleteDto( accountMapper.fromAccountToAutoCompleteDto( student.getAccount() ) );
        studentAutoCompleteDto.setId( student.getId() );

        return studentAutoCompleteDto;
    }

    @Override
    public List<StudentDto> fromStudentListToStudentDtoList(List<Student> list) {
        if ( list == null ) {
            return null;
        }

        List<StudentDto> list1 = new ArrayList<StudentDto>( list.size() );
        for ( Student student : list ) {
            list1.add( fromEntityToStudentDto( student ) );
        }

        return list1;
    }

    @Override
    public List<StudentAutoCompleteDto> fromStudentListToStudentDtoListAutocomplete(List<Student> list) {
        if ( list == null ) {
            return null;
        }

        List<StudentAutoCompleteDto> list1 = new ArrayList<StudentAutoCompleteDto>( list.size() );
        for ( Student student : list ) {
            list1.add( fromStudentToDtoAutoComplete( student ) );
        }

        return list1;
    }
}
