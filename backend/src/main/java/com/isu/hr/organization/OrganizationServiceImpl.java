package com.isu.hr.organization;

import com.isu.hr.organization.dto.OrganizationRequestDto;
import com.isu.hr.organization.dto.OrganizationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponseDto getOrganization(String code) {
        Organization organization = organizationRepository.findOrganizationByCode(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organization not found with code: " + code
                ));

        return OrganizationResponseDto.builder()
                .code(organization.getCode())
                .name(organization.getName())
                .startYmd(organization.getStartYmd())
                .endYmd(organization.getEndYmd())
                .build();
    }

    @Override
    public List<OrganizationResponseDto> getAllOrganizations(List<String> codes) {
        List<Organization> organizations = organizationRepository.findAll().stream()
                .filter(organization -> codes.contains(organization.getCode()))
                .toList();

        if (organizations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No organizations found with provided codes"
            );
        }

        return organizations.stream()
                .map(organization -> OrganizationResponseDto.builder()
                        .code(organization.getCode())
                        .name(organization.getName())
                        .startYmd(organization.getStartYmd())
                        .endYmd(organization.getEndYmd())
                        .build())
                .toList();
    }

    @Override
    public List<OrganizationResponseDto> saveOrganization(List<OrganizationRequestDto> dtos) {
        List<Organization> organizations = dtos.stream()
                .map(dto -> Organization.createNewOrganization(
                        dto.getCode(),
                        dto.getName(),
                        dto.getStartYmd(),
                        dto.getEndYmd()
                )).toList();

        List<Organization> savedOrganizations = organizationRepository.saveAll(organizations);

        return savedOrganizations.stream()
                .map(organization -> OrganizationResponseDto.builder()
                        .code(organization.getCode())
                        .name(organization.getName())
                        .startYmd(organization.getStartYmd())
                        .endYmd(organization.getEndYmd())
                        .build())
                .toList();
    }

    @Override
    public List<OrganizationResponseDto> modifyOrganization(List<OrganizationRequestDto> dtos) {
        List<String> codes = dtos.stream()
                .map(OrganizationRequestDto::getCode)
                .toList();

        List<Organization> organizationsToModify = organizationRepository.findAll().stream()
                .filter(organization -> codes.contains(organization.getCode()))
                .toList();

        if (organizationsToModify.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No organizations found with provided codes"
            );
        }

        for (Organization organization : organizationsToModify) {
            OrganizationRequestDto dto = dtos.stream()
                    .filter(d -> d.getCode().equals(organization.getCode()))
                    .findFirst()
                    .orElse(null);

            if (dto != null) {
                organization.setName(dto.getName());
                organization.setStartYmd(dto.getStartYmd());
                organization.setEndYmd(dto.getEndYmd());
            }
        }

        List<Organization> modifiedOrganizations = organizationRepository.saveAll(organizationsToModify);

        return modifiedOrganizations.stream()
                .map(organization -> OrganizationResponseDto.builder()
                        .code(organization.getCode())
                        .name(organization.getName())
                        .startYmd(organization.getStartYmd())
                        .endYmd(organization.getEndYmd())
                        .build())
                .toList();
    }

    @Override
    public List<OrganizationResponseDto> deleteOrganization(List<OrganizationRequestDto> dtos) {
        List<String> codes = dtos.stream()
                .map(OrganizationRequestDto::getCode)
                .toList();

        List<Organization> organizationsToDelete = organizationRepository.findAll().stream()
                .filter(organization -> codes.contains(organization.getCode()))
                .toList();

        if (organizationsToDelete.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No organizations found with provided codes"
            );
        }

        List<OrganizationResponseDto> deletedOrganizations = organizationsToDelete.stream()
                .map(organization -> OrganizationResponseDto.builder()
                        .code(organization.getCode())
                        .name(organization.getName())
                        .startYmd(organization.getStartYmd())
                        .endYmd(organization.getEndYmd())
                        .build())
                .toList();

        organizationRepository.deleteAll(organizationsToDelete);

        return deletedOrganizations;
    }
}