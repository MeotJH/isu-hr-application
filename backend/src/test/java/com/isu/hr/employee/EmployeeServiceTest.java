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
import static org.mockito.Mockito.verify;

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
    @DisplayName("사번 목록으로 직원들 조회 - 성공")
    void getAllEmployees_Success() {
        // given
        List<String> sabuns = List.of("10001", "10002");
        List<Employee> allEmployees = List.of(
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
                        .build(),
                Employee.builder()
                        .sabun("10003")
                        .name("이테스트")
                        .birYmd("19920303")
                        .empYmd("20250922")
                        .email("lee@test.com")
                        .address("서울시 종로구")
                        .build()
        );

        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // when
        List<EmployeeResponseDto> result = employeeService.getAllEmployees(sabuns);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSabun()).isEqualTo("10001");
        assertThat(result.get(0).getName()).isEqualTo("김테스트");
        assertThat(result.get(0).getEmail()).isEqualTo("kim@test.com");
        assertThat(result.get(1).getSabun()).isEqualTo("10002");
        assertThat(result.get(1).getName()).isEqualTo("박테스트");
        assertThat(result.get(1).getEmail()).isEqualTo("park@test.com");
    }

    @Test
    @DisplayName("사번 목록으로 직원들 조회 - 직원들 없음 예외")
    void getAllEmployees_NotFound() {
        // given
        List<String> sabuns = List.of("99998", "99999");
        when(employeeRepository.findAll()).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> employeeService.getAllEmployees(sabuns))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No employees found with provided sabuns");
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

    @Test
    @DisplayName("직원 수정 - 성공")
    void modifyEmployee_Success() {
        // given
        List<EmployeeRequestDto> requestDtos = List.of(
                EmployeeRequestDto.builder()
                        .sabun("10001")
                        .name("김수정")
                        .birYmd("19900101")
                        .empYmd("20250922")
                        .email("kim.modified@test.com")
                        .address("서울시 종로구")
                        .build(),
                EmployeeRequestDto.builder()
                        .sabun("10002")
                        .name("박수정")
                        .birYmd("19910202")
                        .empYmd("20250922")
                        .email("park.modified@test.com")
                        .address("서울시 마포구")
                        .build()
        );

        List<Employee> allEmployees = List.of(
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
                        .build(),
                Employee.builder()
                        .sabun("10003")
                        .name("이테스트")
                        .birYmd("19920303")
                        .empYmd("20250922")
                        .email("lee@test.com")
                        .address("서울시 종로구")
                        .build()
        );

        List<Employee> modifiedEmployees = List.of(
                Employee.builder()
                        .sabun("10001")
                        .name("김수정")
                        .birYmd("19900101")
                        .empYmd("20250922")
                        .email("kim.modified@test.com")
                        .address("서울시 종로구")
                        .build(),
                Employee.builder()
                        .sabun("10002")
                        .name("박수정")
                        .birYmd("19910202")
                        .empYmd("20250922")
                        .email("park.modified@test.com")
                        .address("서울시 마포구")
                        .build()
        );

        when(employeeRepository.findAll()).thenReturn(allEmployees);
        when(employeeRepository.saveAll(any())).thenReturn(modifiedEmployees);

        // when
        List<EmployeeResponseDto> result = employeeService.modifyEmployee(requestDtos);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSabun()).isEqualTo("10001");
        assertThat(result.get(0).getName()).isEqualTo("김수정");
        assertThat(result.get(0).getEmail()).isEqualTo("kim.modified@test.com");
        assertThat(result.get(0).getAddress()).isEqualTo("서울시 종로구");
        assertThat(result.get(1).getSabun()).isEqualTo("10002");
        assertThat(result.get(1).getName()).isEqualTo("박수정");
        assertThat(result.get(1).getEmail()).isEqualTo("park.modified@test.com");
        assertThat(result.get(1).getAddress()).isEqualTo("서울시 마포구");

        verify(employeeRepository).saveAll(any());
    }

    @Test
    @DisplayName("직원 수정 - 직원 없음 예외")
    void modifyEmployee_NotFound() {
        // given
        List<EmployeeRequestDto> requestDtos = List.of(
                EmployeeRequestDto.builder()
                        .sabun("99999")
                        .name("없는직원")
                        .birYmd("19900101")
                        .empYmd("20250922")
                        .email("none@test.com")
                        .address("없는주소")
                        .build()
        );

        when(employeeRepository.findAll()).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> employeeService.modifyEmployee(requestDtos))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No employees found with provided sabuns");
    }

    @Test
    @DisplayName("직원 삭제 - 성공")
    void deleteEmployee_Success() {
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

        List<Employee> allEmployees = List.of(
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
                        .build(),
                Employee.builder()
                        .sabun("10003")
                        .name("이테스트")
                        .birYmd("19920303")
                        .empYmd("20250922")
                        .email("lee@test.com")
                        .address("서울시 종로구")
                        .build()
        );

        when(employeeRepository.findAll()).thenReturn(allEmployees);

        // when
        List<EmployeeResponseDto> result = employeeService.deleteEmployee(requestDtos);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSabun()).isEqualTo("10001");
        assertThat(result.get(0).getName()).isEqualTo("김테스트");
        assertThat(result.get(1).getSabun()).isEqualTo("10002");
        assertThat(result.get(1).getName()).isEqualTo("박테스트");

        verify(employeeRepository).deleteAll(any());
    }

    @Test
    @DisplayName("직원 삭제 - 직원 없음 예외")
    void deleteEmployee_NotFound() {
        // given
        List<EmployeeRequestDto> requestDtos = List.of(
                EmployeeRequestDto.builder()
                        .sabun("99999")
                        .name("없는직원")
                        .birYmd("19900101")
                        .empYmd("20250922")
                        .email("none@test.com")
                        .address("없는주소")
                        .build()
        );

        when(employeeRepository.findAll()).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> employeeService.deleteEmployee(requestDtos))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No employees found with provided sabuns");
    }
}
