package com.base.auth.form.registration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
@ApiModel
public class RegistrationForm {

    @NotNull(message = "student id cant not be null")
    @ApiModelProperty(name = "studentId", required = true)
    private Long studentId;

    @NotNull(message = "course id cant not be null")
    @ApiModelProperty(name = "courseId", required = true)
    private Long courseId;

    @NotNull(message = "isFinished cant not be null")
    @ApiModelProperty(name = "isFinished", required = true)
    private Boolean isFinished;
}
