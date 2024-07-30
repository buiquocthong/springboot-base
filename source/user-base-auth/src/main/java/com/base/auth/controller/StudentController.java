package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.student.StudentAutoCompleteDto;
import com.base.auth.dto.student.StudentDto;
import com.base.auth.form.student.SignUpStudentForm;
import com.base.auth.form.student.UpdateStudentForm;
import com.base.auth.form.user.LoginForm;
import com.base.auth.mapper.AccountMapper;
import com.base.auth.mapper.StudentMapper;
import com.base.auth.mapper.StudentMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Group;
import com.base.auth.model.Student;
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
@RequestMapping("/v1/student")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private GroupRepository groupRepository;
//    @Autowired
//    private AddressRepository addressRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @PostMapping(value = "/signup", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody SignUpStudentForm signUpStudentForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Account accountByPhone = accountRepository.findAccountByPhone(signUpStudentForm.getPhone());
        if (accountByPhone!=null)
        {
            apiMessageDto.setMessage("phone number already exists");
            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_EXIST);
            apiMessageDto.setResult(false);
            return apiMessageDto;
        }

        if (signUpStudentForm.getEmail()!=null)
        {
            Account accountByEmail = accountRepository.findAccountByEmail(signUpStudentForm.getEmail());
            if (accountByEmail!=null)
            {
                apiMessageDto.setMessage("email already exists");
                apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_EXIST);
                apiMessageDto.setResult(false);
                return apiMessageDto;
            }
        }
        Account account = accountMapper.fromSignUpStudentToAccount(signUpStudentForm);
        account.setPassword(passwordEncoder.encode(signUpStudentForm.getPassword()));
        account.setKind(UserBaseConstant.USER_KIND_USER);
        Group group = groupRepository.findFirstByKind(UserBaseConstant.GROUP_KIND_USER);
        account.setGroup(group);
        account.setStatus(UserBaseConstant.STATUS_ACTIVE);
        accountRepository.save(account);

        Student student = new Student();
        student.setAccount(account);
        student.setBirthday(signUpStudentForm.getBirthday());
        student.setAddress(signUpStudentForm.getAddress());
        student.setIdentification(signUpStudentForm.getIdentification());
        studentRepository.save(student);
        apiMessageDto.setMessage("Sign Up Success");
        return apiMessageDto;
    }

    @PostMapping(value = "/login", produces= MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Account account = accountRepository.findAccountByPhone(loginForm.getPhone());
        if (account==null||!passwordEncoder.matches((loginForm.getPassword()),account.getPassword()))
        {
            apiMessageDto.setMessage("phone number or password is not correct ");
            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_LOGIN_FAILED);
            apiMessageDto.setResult(false);
            return apiMessageDto;
        }
        apiMessageDto.setMessage("Login Success");
        return apiMessageDto;
    }
    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_V')")
    public ApiMessageDto<StudentDto> getStudent(@PathVariable("id") Long id)
    {
        ApiMessageDto<StudentDto> apiMessageDto = new ApiMessageDto<>();
        Student student = studentRepository.findById(id).orElse(null);
        if (student==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found Student");
            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(studentMapper.fromEntityToStudentDto(student));
        apiMessageDto.setMessage("get student success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('S_D')")
    public ApiMessageDto<String> deleteStudent(@PathVariable("id") Long id)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Student student = studentRepository.findById(id).orElse(null);
        if (student==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found Student");
            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Account account = accountRepository.findById(student.getAccount().getId()).orElse(null);
        if (account.getIsSuperAdmin()){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not allow delete super admin");
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_ALLOW_DELETE_SUPPER_ADMIN);
            return apiMessageDto;
        }
//        addressRepository.deleteAllByStudentId(id);
        studentRepository.delete(student);
        serviceRepository.deleteAllByAccountId(account.getId());
        accountRepository.delete(account);
        apiMessageDto.setMessage("Delete student success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_L')")
    public ApiMessageDto<ResponseListDto<List<StudentDto>>> getList(StudentCriteria StudentCriteria , Pageable pageable)
    {

        ApiMessageDto<ResponseListDto<List<StudentDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<StudentDto>> responseListDto = new ResponseListDto<>();
        Page<Student> listStudent = studentRepository.findAll(StudentCriteria.getSpecification(),pageable);
        responseListDto.setContent(studentMapper.fromStudentListToStudentDtoList(listStudent.getContent()));
        responseListDto.setTotalPages(listStudent.getTotalPages());
        responseListDto.setTotalElements(listStudent.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list Student success");
        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<StudentAutoCompleteDto>>> ListAutoComplete(StudentCriteria StudentCriteria)
    {
        ApiMessageDto<ResponseListDto<List<StudentAutoCompleteDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<StudentAutoCompleteDto>> responseListDto = new ResponseListDto<>();
        Pageable pageable = PageRequest.of(0,10);
        Page<Student> listStudent =studentRepository.findAll(StudentCriteria.getSpecification(),pageable);
        responseListDto.setContent(studentMapper.fromStudentListToStudentDtoListAutocomplete(listStudent.getContent()));
        responseListDto.setTotalPages(listStudent.getTotalPages());
        responseListDto.setTotalElements(listStudent.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get success");
        return apiMessageDto;
    }
    @Transactional
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('S_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateStudentForm updateStudentForm, BindingResult bindingResult) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Student student = studentRepository.findById(updateStudentForm.getId()).orElse(null);
        if (student==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found Student");
            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Optional<Student> st = studentRepository.findByIdentification(updateStudentForm.getIdentification());
        if (st.isEmpty())
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found Student");
            apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (!student.getAccount().getPhone().equalsIgnoreCase(updateStudentForm.getPhone()))
        {
            Account account = accountRepository.findAccountByPhone(updateStudentForm.getPhone());
            if(account!=null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_EXIST);
                apiMessageDto.setMessage("Phone is existed");
                return apiMessageDto;
            }
        }
        if (!student.getAccount().getEmail().equalsIgnoreCase(updateStudentForm.getEmail()))
        {
            Account account = accountRepository.findAccountByEmail(updateStudentForm.getEmail());
            if(account!=null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setCode(ErrorCode.STUDENT_ERROR_EXIST);
                apiMessageDto.setMessage("Email is existed");
                return apiMessageDto;
            }

        }

        Account account = accountRepository.findById(student.getAccount().getId()).orElse(null);
        if(StringUtils.isNoneBlank(updateStudentForm.getPassword()))
        {
            account.setPassword(passwordEncoder.encode(updateStudentForm.getPassword()));
        }
        accountMapper.fromUpdateStudentFormToEntity(updateStudentForm,account);
        accountRepository.save(account);

        student.setBirthday(updateStudentForm.getBirthday());
        student.setAddress(updateStudentForm.getAddress());
        student.setIdentification(updateStudentForm.getIdentification());
        studentRepository.save(student);
        apiMessageDto.setMessage("update success");
        return apiMessageDto;
    }





}
