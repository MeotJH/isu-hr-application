package com.isu.hr.employee.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeRequest {
    List<EmployeeRequestDto> employees;
}
