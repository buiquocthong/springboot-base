package com.base.auth.form.student;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ApiModel
public class UpdateStudentForm {

    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long  id;
    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String fullName;
    @NotEmpty(message = "phone cant not be null")
    @ApiModelProperty(name = "phone", required = true)
    private String phone;
    @Email
    @ApiModelProperty(name = "email")
    private String email;
    @ApiModelProperty(name = "password")
    private String password;
    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;
    @ApiModelProperty(name = "birthday")
    private LocalDate birthday;
    @ApiModelProperty(name = "address")
    private String address;
    @ApiModelProperty(name = "identification")
    private String identification;
}
