package com.isu.hr.employee.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder(toBuilder = true)
public class EmployeeRequestDto {

    private String sabun;
    private String name;
    private String birYmd;
    private String empYmd;
    private String email;
    private String address;
}
