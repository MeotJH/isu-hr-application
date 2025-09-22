package com.isu.hr.health;

import com.isu.hr.config.RestDocsTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(HealthCheckController.class)
class HealthCheckControllerTest extends RestDocsTestBase {

    @MockitoBean
    private HealthCheckServiceImpl healthCheckService;

    @Test
    @DisplayName("헬스체크 API - 정상 응답")
    void healthCheck() throws Exception {
        // given
        HealthCheckResponse mockResponse = HealthCheckResponse.up(); // 실제 메서드와 동일하게

        when(healthCheckService.getHealth()).thenReturn(mockResponse);
        
        // when & then
        mockMvc.perform(get("/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // 실제 응답을 콘솔에 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.service").value("HR Management System"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.env").exists())
                .andDo(document("health/check",
                        responseFields(
                                fieldWithPath("status").description("서비스 상태 (UP/DOWN)"),
                                fieldWithPath("timestamp").description("응답 시간 (ISO 8601)"),
                                fieldWithPath("service").description("서비스 명"),
                                fieldWithPath("version").description("서비스 버전"),
                                fieldWithPath("env").description("실행 환경")
                        )));
    }

    @Test
    @DisplayName("헬스체크 API - 디테일 포함 응답 응답")
    void healthCheckDetail() throws Exception {
        // given
        Map<String, Object> details = new HashMap<>();
        details.put("database", new HashMap<String, Object>() {{}});
        details.put("diskSpace", new HashMap<String, Object>() {{}});
        details.put("memory", new HashMap<String, Object>() {{}});
        HealthCheckResponse mockResponse = HealthCheckResponse.upWithDetails(details);

        when(healthCheckService.getDetailedHealth()).thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/health/detail")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // 실제 응답을 콘솔에 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.service").value("HR Management System"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.env").exists())
                .andExpect(jsonPath("$.details").isMap())
                .andDo(document("health/detail",
                        responseFields(
                                fieldWithPath("status").description("서비스 상태 (UP/DOWN)"),
                                fieldWithPath("timestamp").description("응답 시간 (ISO 8601)"),
                                fieldWithPath("service").description("서비스 명"),
                                fieldWithPath("version").description("서비스 버전"),
                                fieldWithPath("env").description("실행 환경"),
                                fieldWithPath("details").description("Map으로 구성된 details들이 있다."),
                                // details 안의 모든 필드들도 문서화
                                fieldWithPath("details.database").description("데이터베이스 상세 정보"),
                                fieldWithPath("details.memory").description("메모리 상세 정보"),
                                fieldWithPath("details.diskSpace").description("디스크 공간 상세 정보")
                        )));
    }
}
