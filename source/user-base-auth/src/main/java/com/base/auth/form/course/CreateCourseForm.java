package com.base.auth.form.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class CreateCourseForm {
    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @NotEmpty(message = "short description cant not be null")
    @ApiModelProperty(name = "shortDescription", required = true)
    private String shortDescription;
    @NotEmpty(message = "description cant not be null")
    @ApiModelProperty(name = "description", required = true)
    private String description;
    @ApiModelProperty(name = "avatar", required = true)
    private String avatar;
    @NotEmpty(message = "banner cant not be null")
    @ApiModelProperty(name = "banner", required = true)
    private String banner;
    @NotEmpty(message = "price cant not be null")
    @ApiModelProperty(name = "price", required = true)
    private Double price;
    @ApiModelProperty(name = "saleOff", required = true)
    private Integer saleOff;
    @NotEmpty(message = "category id cant not be null")
    @ApiModelProperty(name = "categoryId", required = true)
    private Long categoryId;

}

