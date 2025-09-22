package com.isu.hr.employee;

import com.isu.hr.employee.dto.EmployeeRequestDto;
import com.isu.hr.employee.dto.EmployeeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {


    private final EmployeeService employeeService;


    @GetMapping(path = "/{sabun}")
    public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable String sabun) {
        log.info("employee get request: {}", sabun);
        EmployeeResponseDto employee = employeeService.getEmployee(sabun);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<List<EmployeeResponseDto>> saveEmployee(
            @RequestBody List<EmployeeRequestDto> employees
    ) {
        log.info("employee post request: {}", employees.toString());
        List<EmployeeResponseDto> employeeResponseDtos = employeeService.saveEmployee(employees);
        return ResponseEntity.ok(employeeResponseDtos);
    }


}
