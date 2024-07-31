package com.base.auth.controller;


import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.course.CourseDto;
import com.base.auth.exception.BadRequestException;
import com.base.auth.exception.UnauthorizationException;
import com.base.auth.form.course.CreateCourseForm;
import com.base.auth.form.course.UpdateCourseForm;
import com.base.auth.mapper.CourseMapper;
import com.base.auth.model.Category;
import com.base.auth.model.Course;
import com.base.auth.repository.CategoryRepository;
import com.base.auth.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/v1/course")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CourseController extends ABasicController{
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('NEWS_V')")
    public ApiMessageDto<CourseDto> get(@PathVariable("id") Long id) {
        ApiMessageDto<CourseDto> apiMessageDto = new ApiMessageDto<>();
        Course exitingCourse = courseRepository.findById(id).orElse(null);
        if (exitingCourse == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NEWS_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(courseMapper.fromCourseToCourseDto(exitingCourse));
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get course success.");
        return apiMessageDto;
    }


    @PostMapping(value = "/create",  produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('S_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCourseForm courseForm){
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Course existingCourse = courseRepository.findCourseByName(courseForm.getName());
        if (existingCourse!=null){
            apiMessageDto.setMessage("course number already exists");
            apiMessageDto.setCode(ErrorCode.COURSE_ERROR_EXIST);
            apiMessageDto.setResult(false);
            return apiMessageDto;
        }
        Category category = categoryRepository.findById(courseForm.getCategoryId()).orElse(null);
        if (category == null || !Objects.equals(category.getKind(), UserBaseConstant.CATEGORY_KIND_NEWS)) {
            throw new BadRequestException(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
        }
        Course course = courseMapper.fromCreateCourseFormToEntity(courseForm);
        course.setField(category);
        courseRepository.save(course);
        apiMessageDto.setMessage("Create news success");
        return apiMessageDto;
    }
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('NEWS_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCourseForm updateCourseForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Course existingCourse = courseRepository.findById(updateCourseForm.getId()).orElse(null);
        if (existingCourse == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.COURSE_ERROR_EXIST);
            return apiMessageDto;
        }
        if(!existingCourse.getName().equals(updateCourseForm.getName())){
            Boolean existingCourseName = courseRepository.existsByName(updateCourseForm.getName());
            if (existingCourseName) {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Name đã tồn tại, không thể cập nhật");
                apiMessageDto.setCode(ErrorCode.COURSE_ERROR_EXIST);
                return apiMessageDto;
            }
        }
        Category category = categoryRepository.findById(updateCourseForm.getCategoryId()).orElse(null);
        if (category == null) {
            throw new BadRequestException(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
        }
        if(!Objects.equals(updateCourseForm.getCategoryId(), existingCourse.getField().getId())){
            if(!Objects.equals(category.getKind(), UserBaseConstant.CATEGORY_KIND_NEWS)){
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("");
                return apiMessageDto;
            }
        }
        courseMapper.updateCourseFromUpdateCourseForm(updateCourseForm,existingCourse);
        existingCourse.setField(category);
        courseRepository.save(existingCourse);
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Update course success");
        return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('NEWS_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Course exsitingCourse = courseRepository.findById(id).orElse(null);
        if (exsitingCourse == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.COURSE_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        courseRepository.delete(exsitingCourse);
        apiMessageDto.setMessage("Delete course success");
        return apiMessageDto;
    }
}

