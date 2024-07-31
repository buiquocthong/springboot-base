package com.base.auth.controller;

import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.registration.RegistrationDto;
import com.base.auth.dto.student.StudentAutoCompleteDto;
import com.base.auth.dto.student.StudentDto;
import com.base.auth.form.registration.RegistrationForm;
import com.base.auth.form.registration.UpdateRegistrationForm;
import com.base.auth.form.student.UpdateStudentForm;
import com.base.auth.mapper.AccountMapper;
import com.base.auth.mapper.RegistrationMapper;
import com.base.auth.model.*;
import com.base.auth.model.criteria.RegistrationCriteria;
import com.base.auth.model.criteria.StudentCriteria;
import com.base.auth.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/registration")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private RegistrationMapper registrationMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private GroupRepository groupRepository;


    @Autowired
    private ServiceRepository serviceRepository;

    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody RegistrationForm registrationForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Student student = studentRepository.findById(registrationForm.getStudentId()).orElse(null);
        if (student == null) {
            apiMessageDto.setMessage("student not exists");
            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_NOT_FOUND);
            apiMessageDto.setResult(false);
            return apiMessageDto;        }
        Course course = courseRepository.findById(registrationForm.getCourseId()).orElse(null);
        if (course==null)
        {
            apiMessageDto.setMessage("course not exists");
            apiMessageDto.setCode(ErrorCode.COURSE_ERROR_NOT_FOUND);
            apiMessageDto.setResult(false);
            return apiMessageDto;
        }

        Optional<Registration> existingRegistration = registrationRepository.findByStudentIdAndCourseId(registrationForm.getStudentId(), registrationForm.getCourseId());
        if (existingRegistration.isPresent()) {
            apiMessageDto.setMessage("Registration already exists");
            apiMessageDto.setCode(ErrorCode.REGISTRATION_ERROR_EXIST);
            apiMessageDto.setResult(false);
            return apiMessageDto;
        }

//        Registration registration = new Registration();
//        registration.setStudent(student);
//        registration.setCourse(course);
//        registration.setIsFinished(registrationForm.getIsFinished());
//        registrationMapper.fromFormDtoToEntity(registrationForm);
        registrationRepository.save(registrationMapper.fromFormDtoToEntity(registrationForm));

        apiMessageDto.setMessage("Register Success");
        apiMessageDto.setResult(true);
        return apiMessageDto;
    }

//    @PostMapping(value = "/login", produces= MediaType.APPLICATION_JSON_VALUE)
//    public ApiMessageDto<String> login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult)
//    {
//        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
//
//        Account account = accountRepository.findAccountByPhone(loginForm.getPhone());
//        if (account==null||!passwordEncoder.matches((loginForm.getPassword()),account.getPassword()))
//        {
//            apiMessageDto.setMessage("phone number or password is not correct ");
//            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_LOGIN_FAILED);
//            apiMessageDto.setResult(false);
//            return apiMessageDto;
//        }
//        apiMessageDto.setMessage("Login Success");
//        return apiMessageDto;
//    }

    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_V')")
    public ApiMessageDto<RegistrationDto> getRegistration(@PathVariable("id") Long id)
    {
        ApiMessageDto<RegistrationDto> apiMessageDto = new ApiMessageDto<>();
        Registration registration = registrationRepository.findById(id).orElse(null);
        if (registration==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found registration");
            apiMessageDto.setCode(ErrorCode.REGISTRATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(registrationMapper.fromRegistrationToDto(registration));
        apiMessageDto.setMessage("get registration success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('S_D')")
    public ApiMessageDto<String> deleteRegistration(@PathVariable("id") Long id)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Registration registration = registrationRepository.findById(id).orElse(null);
        if (registration==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found registration");
            apiMessageDto.setCode(ErrorCode.REGISTRATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        registrationRepository.delete(registration);
        apiMessageDto.setMessage("Delete registration success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_L')")
    public ApiMessageDto<ResponseListDto<List<RegistrationDto>>> getList(RegistrationCriteria registrationCriteria , Pageable pageable)
    {

        ApiMessageDto<ResponseListDto<List<RegistrationDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<RegistrationDto>> responseListDto = new ResponseListDto<>();
        Page<Registration> listRegistration = registrationRepository.findAll(registrationCriteria.getSpecification(),pageable);
        responseListDto.setContent(registrationMapper.fromListToRegistrationDtoList(listRegistration.getContent()));
        responseListDto.setTotalPages(listRegistration.getTotalPages());
        responseListDto.setTotalElements(listRegistration.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list registration success");
        return apiMessageDto;
    }

//    @GetMapping(value = "/auto-complete",produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiMessageDto<ResponseListDto<List<StudentAutoCompleteDto>>> ListAutoComplete(StudentCriteria StudentCriteria)
//    {
//        ApiMessageDto<ResponseListDto<List<StudentAutoCompleteDto>>> apiMessageDto = new ApiMessageDto<>();
//        ResponseListDto<List<StudentAutoCompleteDto>> responseListDto = new ResponseListDto<>();
//        Pageable pageable = PageRequest.of(0,10);
//        Page<Student> listStudent =studentRepository.findAll(StudentCriteria.getSpecification(),pageable);
//        responseListDto.setContent(studentMapper.fromStudentListToStudentDtoListAutocomplete(listStudent.getContent()));
//        responseListDto.setTotalPages(listStudent.getTotalPages());
//        responseListDto.setTotalElements(listStudent.getTotalElements());
//
//        apiMessageDto.setData(responseListDto);
//        apiMessageDto.setMessage("get success");
//        return apiMessageDto;
//    }
    @Transactional
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateRegistrationForm updateRegistrationForm, BindingResult bindingResult) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

//        if (bindingResult.hasErrors()) {
//            apiMessageDto.setResult(false);
//            apiMessageDto.setMessage("Validation error");
//            apiMessageDto.setCode(ErrorCode.VALIDATION_ERROR);
//            return apiMessageDto;
//        }

        Registration registration = registrationRepository.findById(updateRegistrationForm.getId()).orElse(null);
        if (registration == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found registration");
            apiMessageDto.setCode(ErrorCode.REGISTRATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Optional<Registration> existingRegistration = registrationRepository.findByStudentIdAndCourseId(updateRegistrationForm.getStudentId(), updateRegistrationForm.getCourseId());
        if (existingRegistration.isPresent() && !existingRegistration.get().getId().equals(updateRegistrationForm.getId())) {
            apiMessageDto.setMessage("Registration already exists");
            apiMessageDto.setCode(ErrorCode.REGISTRATION_ERROR_EXIST);
            apiMessageDto.setResult(false);
            return apiMessageDto;
        }

        // Tìm Student và Course từ repository
        Student student = studentRepository.findById(updateRegistrationForm.getStudentId()).orElse(null);
        Course course = courseRepository.findById(updateRegistrationForm.getCourseId()).orElse(null);

        if (student == null || course == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Invalid student or course ID");
            apiMessageDto.setCode(ErrorCode.INVALID_STUDENT_OR_COURSE);
            return apiMessageDto;
        }

        // Cập nhật Registration bằng mapper
        registrationMapper.updateFromUpdateRegistrationForm(updateRegistrationForm, registration);

        // Đảm bảo student và course được set đúng
        registration.setStudent(student);
        registration.setCourse(course);

        registrationRepository.save(registration);

        apiMessageDto.setMessage("Update success");
        apiMessageDto.setResult(true);
        return apiMessageDto;
    }


}
