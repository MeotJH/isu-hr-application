package com.isu.hr.organization;

import com.isu.hr.organization.dto.OrganizationRequestDto;
import com.isu.hr.organization.dto.OrganizationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {


    private final OrganizationService organizationService;


    @GetMapping(path = "/{code}")
    public ResponseEntity<OrganizationResponseDto> getOrganization(@PathVariable String code) {
        log.info("organization get request: {}", code);
        OrganizationResponseDto organization = organizationService.getOrganization(code);
        return ResponseEntity.ok(organization);
    }

    @PostMapping(path = "/list")
    public ResponseEntity<List<OrganizationResponseDto>> getAllOrganizations(
            @RequestBody List<String> codes
    ) {
        log.info("organization list request: {}", codes.toString());
        List<OrganizationResponseDto> organizations = organizationService.getAllOrganizations(codes);
        return ResponseEntity.ok(organizations);
    }

    @PostMapping
    public ResponseEntity<List<OrganizationResponseDto>> saveOrganization(
            @RequestBody List<OrganizationRequestDto> organizations
    ) {
        log.info("organization post request: {}", organizations.toString());
        List<OrganizationResponseDto> organizationResponseDtos = organizationService.saveOrganization(organizations);
        return ResponseEntity.ok(organizationResponseDtos);
    }

    @PatchMapping
    public ResponseEntity<List<OrganizationResponseDto>> modifyOrganization(
            @RequestBody List<OrganizationRequestDto> organizations
    ) {
        log.info("organization patch request: {}", organizations.toString());
        List<OrganizationResponseDto> organizationResponseDtos = organizationService.modifyOrganization(organizations);
        return ResponseEntity.ok(organizationResponseDtos);
    }

    @DeleteMapping
    public ResponseEntity<List<OrganizationResponseDto>> deleteOrganization(
            @RequestBody List<OrganizationRequestDto> organizations
    ) {
        log.info("organization delete request: {}", organizations.toString());
        List<OrganizationResponseDto> organizationResponseDtos = organizationService.deleteOrganization(organizations);
        return ResponseEntity.ok(organizationResponseDtos);
    }


}