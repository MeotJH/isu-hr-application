package com.isu.hr.organization;

import com.isu.hr.organization.dto.OrganizationRequestDto;
import com.isu.hr.organization.dto.OrganizationResponseDto;
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
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Test
    @DisplayName("코드로 조직 조회 - 성공")
    void getOrganization_Success() {
        // given
        String code = "ORG001";
        Organization organization = Organization.builder()
                .code(code)
                .name("테스트조직")
                .startYmd("20250101")
                .endYmd("99991231")
                .build();

        when(organizationRepository.findOrganizationByCode(code))
                .thenReturn(Optional.of(organization));

        // when
        OrganizationResponseDto result = organizationService.getOrganization(code);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(code);
        assertThat(result.getName()).isEqualTo("테스트조직");
        assertThat(result.getStartYmd()).isEqualTo("20250101");
        assertThat(result.getEndYmd()).isEqualTo("99991231");
    }

    @Test
    @DisplayName("코드로 조직 조회 - 조직 없음 예외")
    void getOrganization_NotFound() {
        // given
        String code = "ORG999";
        when(organizationRepository.findOrganizationByCode(code))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> organizationService.getOrganization(code))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Organization not found with code: " + code);
    }

    @Test
    @DisplayName("코드 목록으로 조직들 조회 - 성공")
    void getAllOrganizations_Success() {
        // given
        List<String> codes = List.of("ORG001", "ORG002");
        List<Organization> allOrganizations = List.of(
                Organization.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                Organization.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build(),
                Organization.builder()
                        .code("ORG003")
                        .name("테스트조직3")
                        .startYmd("20250301")
                        .endYmd("99991231")
                        .build()
        );

        when(organizationRepository.findAll()).thenReturn(allOrganizations);

        // when
        List<OrganizationResponseDto> result = organizationService.getAllOrganizations(codes);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCode()).isEqualTo("ORG001");
        assertThat(result.get(0).getName()).isEqualTo("테스트조직1");
        assertThat(result.get(1).getCode()).isEqualTo("ORG002");
        assertThat(result.get(1).getName()).isEqualTo("테스트조직2");
    }

    @Test
    @DisplayName("코드 목록으로 조직들 조회 - 조직들 없음 예외")
    void getAllOrganizations_NotFound() {
        // given
        List<String> codes = List.of("ORG998", "ORG999");
        when(organizationRepository.findAll()).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> organizationService.getAllOrganizations(codes))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No organizations found with provided codes");
    }

    @Test
    @DisplayName("조직 저장 - 성공")
    void saveOrganization_Success() {
        // given
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

        List<Organization> savedOrganizations = List.of(
                Organization.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                Organization.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build()
        );

        when(organizationRepository.saveAll(any())).thenReturn(savedOrganizations);

        // when
        List<OrganizationResponseDto> result = organizationService.saveOrganization(requestDtos);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCode()).isEqualTo("ORG001");
        assertThat(result.get(0).getName()).isEqualTo("테스트조직1");
        assertThat(result.get(1).getCode()).isEqualTo("ORG002");
        assertThat(result.get(1).getName()).isEqualTo("테스트조직2");
    }

    @Test
    @DisplayName("조직 수정 - 성공")
    void modifyOrganization_Success() {
        // given
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

        List<Organization> allOrganizations = List.of(
                Organization.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                Organization.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build(),
                Organization.builder()
                        .code("ORG003")
                        .name("테스트조직3")
                        .startYmd("20250301")
                        .endYmd("99991231")
                        .build()
        );

        List<Organization> modifiedOrganizations = List.of(
                Organization.builder()
                        .code("ORG001")
                        .name("수정된조직1")
                        .startYmd("20250101")
                        .endYmd("20251231")
                        .build(),
                Organization.builder()
                        .code("ORG002")
                        .name("수정된조직2")
                        .startYmd("20250201")
                        .endYmd("20261231")
                        .build()
        );

        when(organizationRepository.findAll()).thenReturn(allOrganizations);
        when(organizationRepository.saveAll(any())).thenReturn(modifiedOrganizations);

        // when
        List<OrganizationResponseDto> result = organizationService.modifyOrganization(requestDtos);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCode()).isEqualTo("ORG001");
        assertThat(result.get(0).getName()).isEqualTo("수정된조직1");
        assertThat(result.get(0).getEndYmd()).isEqualTo("20251231");
        assertThat(result.get(1).getCode()).isEqualTo("ORG002");
        assertThat(result.get(1).getName()).isEqualTo("수정된조직2");
        assertThat(result.get(1).getEndYmd()).isEqualTo("20261231");

        verify(organizationRepository).saveAll(any());
    }

    @Test
    @DisplayName("조직 수정 - 조직 없음 예외")
    void modifyOrganization_NotFound() {
        // given
        List<OrganizationRequestDto> requestDtos = List.of(
                OrganizationRequestDto.builder()
                        .code("ORG999")
                        .name("없는조직")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build()
        );

        when(organizationRepository.findAll()).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> organizationService.modifyOrganization(requestDtos))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No organizations found with provided codes");
    }

    @Test
    @DisplayName("조직 삭제 - 성공")
    void deleteOrganization_Success() {
        // given
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

        List<Organization> allOrganizations = List.of(
                Organization.builder()
                        .code("ORG001")
                        .name("테스트조직1")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build(),
                Organization.builder()
                        .code("ORG002")
                        .name("테스트조직2")
                        .startYmd("20250201")
                        .endYmd("99991231")
                        .build(),
                Organization.builder()
                        .code("ORG003")
                        .name("테스트조직3")
                        .startYmd("20250301")
                        .endYmd("99991231")
                        .build()
        );

        when(organizationRepository.findAll()).thenReturn(allOrganizations);

        // when
        List<OrganizationResponseDto> result = organizationService.deleteOrganization(requestDtos);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCode()).isEqualTo("ORG001");
        assertThat(result.get(0).getName()).isEqualTo("테스트조직1");
        assertThat(result.get(1).getCode()).isEqualTo("ORG002");
        assertThat(result.get(1).getName()).isEqualTo("테스트조직2");

        verify(organizationRepository).deleteAll(any());
    }

    @Test
    @DisplayName("조직 삭제 - 조직 없음 예외")
    void deleteOrganization_NotFound() {
        // given
        List<OrganizationRequestDto> requestDtos = List.of(
                OrganizationRequestDto.builder()
                        .code("ORG999")
                        .name("없는조직")
                        .startYmd("20250101")
                        .endYmd("99991231")
                        .build()
        );

        when(organizationRepository.findAll()).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> organizationService.deleteOrganization(requestDtos))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No organizations found with provided codes");
    }
}