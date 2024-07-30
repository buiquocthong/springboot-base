package com.base.auth.dto.student;

import com.base.auth.dto.account.AccountDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class StudentDto {

    private Long id;
    private LocalDate birthday;
    private AccountDto account;
    private String address;
    private String identification;
}
