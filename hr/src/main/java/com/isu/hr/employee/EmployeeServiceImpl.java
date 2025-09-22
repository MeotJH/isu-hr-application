package com.isu.hr.employee;

import com.isu.hr.employee.dto.EmployeeRequestDto;
import com.isu.hr.employee.dto.EmployeeResponseDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponseDto getEmployee(String sabun) {
        Employee employee = employeeRepository.findEmployeeBySabun(sabun)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found with sabun: " + sabun
                ));

        return EmployeeResponseDto.builder()
                .name(employee.getName())
                .sabun(employee.getSabun())
                .address(employee.getAddress())
                .email(employee.getEmail())
                .birYmd(employee.getBirYmd())
                .empYmd(employee.getEmpYmd())
                .build();
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees(List<String> sabuns) {
        return List.of();
    }

    @Override
    public List<EmployeeResponseDto> saveEmployee(List<EmployeeRequestDto> dtos) {
        List<Employee> employees = dtos.stream()
                .map(dto -> Employee.createNewEmployee(
                        dto.getSabun(),
                        dto.getName(),
                        dto.getBirYmd(),
                        dto.getEmpYmd(),
                        dto.getEmail(),
                        dto.getAddress()
                )).toList();

        List<Employee> savedEmployees = employeeRepository.saveAll(employees);

        return savedEmployees.stream()
                .map(employee -> EmployeeResponseDto.builder()
                        .sabun(employee.getSabun())
                        .name(employee.getName())
                        .birYmd(employee.getBirYmd())
                        .empYmd(employee.getEmpYmd())
                        .email(employee.getEmail())
                        .address(employee.getAddress())
                        .build())
                .toList();
    }

    @Override
    public List<EmployeeResponseDto> deleteEmployee(List<EmployeeRequestDto> dtos) {
        return List.of();
    }
}
