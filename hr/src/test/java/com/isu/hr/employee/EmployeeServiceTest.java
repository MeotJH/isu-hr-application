package com.isu.hr.employee;

import com.isu.hr.employee.dto.EmployeeRequestDto;
import com.isu.hr.employee.dto.EmployeeResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("사번으로 직원 조회 - 성공")
    void getEmployee_Success() {
        // given
        String sabun = "10000";
        Employee employee = Employee.builder()
                .sabun(sabun)
                .name("김테스트")
                .birYmd("19900101")
                .empYmd("20250922")
                .email("test@example.com")
                .address("서울시 강남구")
                .build();

        when(employeeRepository.findEmployeeBySabun(sabun))
                .thenReturn(Optional.of(employee));

        // when
        EmployeeResponseDto result = employeeService.getEmployee(sabun);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getSabun()).isEqualTo(sabun);
        assertThat(result.getName()).isEqualTo("김테스트");
        assertThat(result.getBirYmd()).isEqualTo("19900101");
        assertThat(result.getEmpYmd()).isEqualTo("20250922");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getAddress()).isEqualTo("서울시 강남구");
    }

    @Test
    @DisplayName("사번으로 직원 조회 - 직원 없음 예외")
    void getEmployee_NotFound() {
        // given
        String sabun = "99999";
        when(employeeRepository.findEmployeeBySabun(sabun))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> employeeService.getEmployee(sabun))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Employee not found with sabun: " + sabun);
    }

    @Test
    @DisplayName("직원 저장 - 성공")
    void saveEmployee_Success() {
        // given
        List<EmployeeRequestDto> requestDtos = List.of(
                EmployeeRequestDto.builder()
                        .sabun("10001")
                        .name("김테스트")
                        .birYmd("19900101")
                        .empYmd("20250922")
                        .email("kim@test.com")
                        .address("서울시 강남구")
                        .build(),
                EmployeeRequestDto.builder()
                        .sabun("10002")
                        .name("박테스트")
                        .birYmd("19910202")
                        .empYmd("20250922")
                        .email("park@test.com")
                        .address("서울시 서초구")
                        .build()
        );

        List<Employee> savedEmployees = List.of(
                Employee.builder()
                        .sabun("10001")
                        .name("김테스트")
                        .birYmd("19900101")
                        .empYmd("20250922")
                        .email("kim@test.com")
                        .address("서울시 강남구")
                        .build(),
                Employee.builder()
                        .sabun("10002")
                        .name("박테스트")
                        .birYmd("19910202")
                        .empYmd("20250922")
                        .email("park@test.com")
                        .address("서울시 서초구")
                        .build()
        );

        when(employeeRepository.saveAll(any())).thenReturn(savedEmployees);

        // when
        List<EmployeeResponseDto> result = employeeService.saveEmployee(requestDtos);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSabun()).isEqualTo("10001");
        assertThat(result.get(0).getName()).isEqualTo("김테스트");
        assertThat(result.get(1).getSabun()).isEqualTo("10002");
        assertThat(result.get(1).getName()).isEqualTo("박테스트");
    }
}
