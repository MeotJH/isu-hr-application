package com.isu.hr.organization;

import com.isu.hr.organization.dto.OrganizationRequestDto;
import com.isu.hr.organization.dto.OrganizationResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrganizationService {

    OrganizationResponseDto getOrganization(String code);

    List<OrganizationResponseDto> getAllOrganizations(List<String> codes);

    List<OrganizationResponseDto> saveOrganization(List<OrganizationRequestDto> dtos);

    List<OrganizationResponseDto> modifyOrganization(List<OrganizationRequestDto> dtos);

    List<OrganizationResponseDto> deleteOrganization(List<OrganizationRequestDto> dtos);

}