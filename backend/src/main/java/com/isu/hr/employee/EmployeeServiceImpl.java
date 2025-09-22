package com.isu.hr.employee;

import com.isu.hr.employee.dto.EmployeeRequestDto;
import com.isu.hr.employee.dto.EmployeeResponseDto;
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
        List<Employee> employees = employeeRepository.findAll().stream()
                .filter(employee -> sabuns.contains(employee.getSabun()))
                .toList();

        if (employees.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No employees found with provided sabuns"
            );
        }

        return employees.stream()
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
    public List<EmployeeResponseDto> modifyEmployee(List<EmployeeRequestDto> dtos) {
        List<String> sabuns = dtos.stream()
                .map(EmployeeRequestDto::getSabun)
                .toList();

        List<Employee> employeesToModify = employeeRepository.findAll().stream()
                .filter(employee -> sabuns.contains(employee.getSabun()))
                .toList();

        if (employeesToModify.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No employees found with provided sabuns"
            );
        }

        for (Employee employee : employeesToModify) {
            EmployeeRequestDto dto = dtos.stream()
                    .filter(d -> d.getSabun().equals(employee.getSabun()))
                    .findFirst()
                    .orElse(null);

            if (dto != null) {
                employee.setName(dto.getName());
                employee.setBirYmd(dto.getBirYmd());
                employee.setEmpYmd(dto.getEmpYmd());
                employee.setEmail(dto.getEmail());
                employee.setAddress(dto.getAddress());
            }
        }

        List<Employee> modifiedEmployees = employeeRepository.saveAll(employeesToModify);

        return modifiedEmployees.stream()
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
        List<String> sabuns = dtos.stream()
                .map(EmployeeRequestDto::getSabun)
                .toList();

        List<Employee> employeesToDelete = employeeRepository.findAll().stream()
                .filter(employee -> sabuns.contains(employee.getSabun()))
                .toList();

        if (employeesToDelete.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No employees found with provided sabuns"
            );
        }

        List<EmployeeResponseDto> deletedEmployees = employeesToDelete.stream()
                .map(employee -> EmployeeResponseDto.builder()
                        .sabun(employee.getSabun())
                        .name(employee.getName())
                        .birYmd(employee.getBirYmd())
                        .empYmd(employee.getEmpYmd())
                        .email(employee.getEmail())
                        .address(employee.getAddress())
                        .build())
                .toList();

        employeeRepository.deleteAll(employeesToDelete);

        return deletedEmployees;
    }
}
