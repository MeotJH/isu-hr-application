package com.isu.hr.employee.dto;


import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class EmployeeResponseDto {
    private String sabun;
    private String name;
    private String birYmd;
    private String empYmd;
    private String email;
    private String address;


}