package com.base.auth.dto.course;

import com.base.auth.dto.category.CategoryDto;
import lombok.Data;

@Data
public class CourseDto {
    private Long id;

    private String name;

    private String shortDescription;

    private String description;

    private String avatar;

    private String banner;

    private Double price;

    private Integer saleOff;

    private CategoryDto field;
}
