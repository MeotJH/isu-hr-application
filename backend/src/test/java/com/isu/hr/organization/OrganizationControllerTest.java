package com.isu.hr.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.hr.config.RestDocsTestBase;
import com.isu.hr.organization.dto.OrganizationRequestDto;
import com.isu.hr.organization.dto.OrganizationResponseDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrganizationController.class)
public class OrganizationControllerTest extends RestDocsTestBase {


    @MockitoBean
    private OrganizationService service;

    @Test
    @DisplayName("조직 1건 가져오기 API - 정상 응답")
    void getOrganizationTest() throws Exception {
        //given
        String code = "ORG001";
        OrganizationResponseDto dto = OrganizationResponseDto.builder()
                .code(code)
                .name("테스트조직")
                .startYmd("20250101")
                .endYmd("99991231")
                .build();
        when(service.getOrganization(code)).thenReturn(dto);

        //when  //then
        mockMvc.perform(get("/organization/"+code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ORG001"))
                .andExpect(jsonPath("$.name").value("테스트조직"))
                .andExpect(jsonPath("$.startYmd").value("20250101"))
                .andExpect(jsonPath("$.endYmd").value("99991231"))
                .andDo(document("organization/one",
                        responseFields(
                                fieldWithPath("code").description("조직의 코드"),
                                fieldWithPath("name").description("조직의 이름"),
                                fieldWithPath("startYmd").description("조직의 시작일"),
                                fieldWithPath("endYmd").description("조직의 종료일")
                        )));


    }

    @Test
    @DisplayName("조직들 가져오기 API - 정상 응답")
    void getAllOrganizationsTest() throws Exception {
        //given
        List<String> codes = List.of("ORG001", "ORG002");
        List<OrganizationResponseDto> responseDtos = List.of(
                OrganizationResponseDto.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                OrganizationResponseDto.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build()
        );

        when(service.getAllOrganizations(codes)).thenReturn(responseDtos);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(codes);

        //when & then
        mockMvc.perform(post("/organization/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("ORG001"))
                .andExpect(jsonPath("$[0].name").value("테스트조직1"))
                .andExpect(jsonPath("$[1].code").value("ORG002"))
                .andExpect(jsonPath("$[1].name").value("테스트조직2"))
                .andDo(document("organization/list",
                        requestFields(
                                fieldWithPath("[]").description("조회할 조직들의 코드 목록")
                        ),
                        responseFields(
                                fieldWithPath("[].code").description("조회된 조직의 코드"),
                                fieldWithPath("[].name").description("조회된 조직의 이름"),
                                fieldWithPath("[].startYmd").description("조회된 조직의 시작일"),
                                fieldWithPath("[].endYmd").description("조회된 조직의 종료일")
                        )));
    }

    @Test
    @DisplayName("조직 저장하기 API - 정상 응답")
    void saveOrganizationTest() throws Exception {
        //given
        List<OrganizationRequestDto> requestDtos = List.of(
                OrganizationRequestDto.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                OrganizationRequestDto.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build()
        );

        List<OrganizationResponseDto> responseDtos = List.of(
                OrganizationResponseDto.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                OrganizationResponseDto.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build()
        );

        when(service.saveOrganization(any())).thenReturn(responseDtos);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDtos);

        //when & then
        mockMvc.perform(post("/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("ORG001"))
                .andExpect(jsonPath("$[0].name").value("테스트조직1"))
                .andExpect(jsonPath("$[1].code").value("ORG002"))
                .andExpect(jsonPath("$[1].name").value("테스트조직2"))
                .andDo(document("organization/save",
                        requestFields(
                                fieldWithPath("[].code").description("조직의 코드"),
                                fieldWithPath("[].name").description("조직의 이름"),
                                fieldWithPath("[].startYmd").description("조직의 시작일"),
                                fieldWithPath("[].endYmd").description("조직의 종료일")
                        ),
                        responseFields(
                                fieldWithPath("[].code").description("저장된 조직의 코드"),
                                fieldWithPath("[].name").description("저장된 조직의 이름"),
                                fieldWithPath("[].startYmd").description("저장된 조직의 시작일"),
                                fieldWithPath("[].endYmd").description("저장된 조직의 종료일")
                        )));
    }

    @Test
    @DisplayName("조직 수정하기 API - 정상 응답")
    void modifyOrganizationTest() throws Exception {
        //given
        List<OrganizationRequestDto> requestDtos = List.of(
                OrganizationRequestDto.builder()
                        .code("ORG001")
                        .name("수정된조직1")
                        .startYmd("20250101")
                        .endYmd("20251231")
                        .build(),
                OrganizationRequestDto.builder()
                        .code("ORG002")
                        .name("수정된조직2")
                        .startYmd("20250201")
                        .endYmd("20261231")
                        .build()
        );

        List<OrganizationResponseDto> responseDtos = List.of(
                OrganizationResponseDto.builder()
                        .code("ORG001")
                        .name("수정된조직1")
                        .startYmd("20250101")
                        .endYmd("20251231")
                        .build(),
                OrganizationResponseDto.builder()
                        .code("ORG002")
                        .name("수정된조직2")
                        .startYmd("20250201")
                        .endYmd("20261231")
                        .build()
        );

        when(service.modifyOrganization(any())).thenReturn(responseDtos);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDtos);

        //when & then
        mockMvc.perform(patch("/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("ORG001"))
                .andExpect(jsonPath("$[0].name").value("수정된조직1"))
                .andExpect(jsonPath("$[0].endYmd").value("20251231"))
                .andExpect(jsonPath("$[1].code").value("ORG002"))
                .andExpect(jsonPath("$[1].name").value("수정된조직2"))
                .andExpect(jsonPath("$[1].endYmd").value("20261231"))
                .andDo(document("organization/modify",
                        requestFields(
                                fieldWithPath("[].code").description("수정할 조직의 코드"),
                                fieldWithPath("[].name").description("수정할 조직의 이름"),
                                fieldWithPath("[].startYmd").description("수정할 조직의 시작일"),
                                fieldWithPath("[].endYmd").description("수정할 조직의 종료일")
                        ),
                        responseFields(
                                fieldWithPath("[].code").description("수정된 조직의 코드"),
                                fieldWithPath("[].name").description("수정된 조직의 이름"),
                                fieldWithPath("[].startYmd").description("수정된 조직의 시작일"),
                                fieldWithPath("[].endYmd").description("수정된 조직의 종료일")
                        )));
    }

    @Test
    @DisplayName("조직 삭제하기 API - 정상 응답")
    void deleteOrganizationTest() throws Exception {
        //given
        List<OrganizationRequestDto> requestDtos = List.of(
                OrganizationRequestDto.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                OrganizationRequestDto.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build()
        );

        List<OrganizationResponseDto> responseDtos = List.of(
                OrganizationResponseDto.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                OrganizationResponseDto.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build()
        );

        when(service.deleteOrganization(any())).thenReturn(responseDtos);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDtos);

        //when & then
        mockMvc.perform(delete("/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("ORG001"))
                .andExpect(jsonPath("$[0].name").value("테스트조직1"))
                .andExpect(jsonPath("$[1].code").value("ORG002"))
                .andExpect(jsonPath("$[1].name").value("테스트조직2"))
                .andDo(document("organization/delete",
                        requestFields(
                                fieldWithPath("[].code").description("삭제할 조직의 코드"),
                                fieldWithPath("[].name").description("삭제할 조직의 이름"),
                                fieldWithPath("[].startYmd").description("삭제할 조직의 시작일"),
                                fieldWithPath("[].endYmd").description("삭제할 조직의 종료일")
                        ),
                        responseFields(
                                fieldWithPath("[].code").description("삭제된 조직의 코드"),
                                fieldWithPath("[].name").description("삭제된 조직의 이름"),
                                fieldWithPath("[].startYmd").description("삭제된 조직의 시작일"),
                                fieldWithPath("[].endYmd").description("삭제된 조직의 종료일")
                        )));
    }
}