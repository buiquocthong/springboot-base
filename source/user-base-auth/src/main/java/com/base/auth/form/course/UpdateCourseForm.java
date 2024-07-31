package com.base.auth.form.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateCourseForm {
    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;
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
    @NotNull(message = "price cant not be null")
    @ApiModelProperty(name = "price", required = true)
    private Double price;
    @ApiModelProperty(name = "saleOff", required = true)
    private Integer saleOff;
    @NotNull(message = "category id cant not be null")
    @ApiModelProperty(name = "categoryId", required = true)
    private Long categoryId;
}

