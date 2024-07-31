package com.base.auth.mapper;

import com.base.auth.dto.course.CourseDto;
import com.base.auth.form.course.CreateCourseForm;
import com.base.auth.form.course.UpdateCourseForm;
import com.base.auth.form.news.CreateNewsForm;
import com.base.auth.form.news.UpdateNewsForm;
import com.base.auth.model.Course;
import com.base.auth.model.News;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface CourseMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "banner", target = "banner")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "field", target = "field", qualifiedByName = "fromEntityToCategoryDto")
    @Mapping(source = "saleOff", target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCourseToDto")
    CourseDto fromCourseToCourseDto(Course course);

    @IterableMapping(elementTargetType = CourseDto.class, qualifiedByName = "fromCourseToDto")
    List<CourseDto> fromCoursesToCourseDto(List<Course> courses);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "banner", target = "banner")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "saleOff", target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    Course fromCreateCourseFormToEntity(CreateCourseForm courseForm);
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "banner", target = "banner")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "saleOff", target = "saleOff")
    @Named("updateCourseNotStartedFromForm")
    void updateCourseFromUpdateCourseForm(UpdateCourseForm updateCourseForm, @MappingTarget Course course);
}
