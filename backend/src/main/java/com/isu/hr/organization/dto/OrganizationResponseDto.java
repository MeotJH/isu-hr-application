package com.isu.hr.organization.dto;


import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class OrganizationResponseDto {
    private String code;
    private String name;
    private String startYmd;
    private String endYmd;


}