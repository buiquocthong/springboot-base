package com.base.auth.form.registration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateRegistrationForm {
    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "studentId", required = true)
    private Long studentId;

    @ApiModelProperty(name = "courseId", required = true)
    private Long courseId;

    @ApiModelProperty(name = "isFinished", required = true)
    private Boolean isFinished;
}
