package com.isu.hr.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.hr.config.RestDocsTestBase;
import com.isu.hr.employee.dto.EmployeeRequestDto;
import com.isu.hr.employee.dto.EmployeeResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest extends RestDocsTestBase {


    @MockitoBean
    private EmployeeService service;

    @Test
    @DisplayName("직원 1건 가져오기 API - 정상 응답")
    void getEmployeeTest() throws Exception {
        //given
        String sabun = "10000";
        EmployeeResponseDto dto = EmployeeResponseDto.builder()
                .sabun(sabun)
                .name("테스트밸류")
                .birYmd("19900000")
                .empYmd("20250922")
                .email("name@mail.dot")
                .address("테스트주소")
                .build();
        when(service.getEmployee(sabun)).thenReturn(dto);

        //when  //then
        mockMvc.perform(get("/employee/"+sabun)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // 실제 응답을 콘솔에 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sabun").value("10000"))
                .andExpect(jsonPath("$.name").value("테스트밸류"))
                .andExpect(jsonPath("$.birYmd").value("19900000"))
                .andExpect(jsonPath("$.empYmd").value("20250922"))
                .andExpect(jsonPath("$.email").value("name@mail.dot"))
                .andExpect(jsonPath("$.address").value("테스트주소"))
                .andDo(document("employee/one",
                        responseFields(
                                fieldWithPath("sabun").description("직원의 사번"),
                                fieldWithPath("name").description("직원의 이름"),
                                fieldWithPath("birYmd").description("직원의 생년월일"),
                                fieldWithPath("empYmd").description("직원의 입사일"),
                                fieldWithPath("email").description("직원의 이메일 주소"),
                                fieldWithPath("address").description("직원의 거주지")
                        )));


    }

    @Test
    @DisplayName("직원 저장하기 API - 정상 응답")
    void saveEmployeeTest() throws Exception {
        //given
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

        List<EmployeeResponseDto> responseDtos = List.of(
                EmployeeResponseDto.builder()
                        .sabun("10001")
                        .name("김테스트")
                        .birYmd("19900101")
                        .empYmd("20250922")
                        .email("kim@test.com")
                        .address("서울시 강남구")
                        .build(),
                EmployeeResponseDto.builder()
                        .sabun("10002")
                        .name("박테스트")
                        .birYmd("19910202")
                        .empYmd("20250922")
                        .email("park@test.com")
                        .address("서울시 서초구")
                        .build()
        );

        when(service.saveEmployee(any())).thenReturn(responseDtos);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDtos);

        //when & then
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].sabun").value("10001"))
                .andExpect(jsonPath("$[0].name").value("김테스트"))
                .andExpect(jsonPath("$[1].sabun").value("10002"))
                .andExpect(jsonPath("$[1].name").value("박테스트"))
                .andDo(document("employee/save",
                        requestFields(
                                fieldWithPath("[].sabun").description("직원의 사번"),
                                fieldWithPath("[].name").description("직원의 이름"),
                                fieldWithPath("[].birYmd").description("직원의 생년월일"),
                                fieldWithPath("[].empYmd").description("직원의 입사일"),
                                fieldWithPath("[].email").description("직원의 이메일 주소"),
                                fieldWithPath("[].address").description("직원의 거주지")
                        ),
                        responseFields(
                                fieldWithPath("[].sabun").description("저장된 직원의 사번"),
                                fieldWithPath("[].name").description("저장된 직원의 이름"),
                                fieldWithPath("[].birYmd").description("저장된 직원의 생년월일"),
                                fieldWithPath("[].empYmd").description("저장된 직원의 입사일"),
                                fieldWithPath("[].email").description("저장된 직원의 이메일 주소"),
                                fieldWithPath("[].address").description("저장된 직원의 거주지")
                        )));
    }
}
