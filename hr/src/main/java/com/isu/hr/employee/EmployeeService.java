package com.isu.hr.employee;

import com.isu.hr.employee.dto.EmployeeRequestDto;
import com.isu.hr.employee.dto.EmployeeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    EmployeeResponseDto getEmployee(String sabun);

    List<EmployeeResponseDto> getAllEmployees(List<String> sabuns);

    List<EmployeeResponseDto> saveEmployee(List<EmployeeRequestDto> dtos);

    List<EmployeeResponseDto> modifyEmployee(List<EmployeeRequestDto> dtos);

    List<EmployeeResponseDto> deleteEmployee(List<EmployeeRequestDto> dtos);

}
