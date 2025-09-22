package com.isu.hr.organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder(toBuilder = true)
public class OrganizationRequestDto {

    private String code;
    private String name;
    private String startYmd;
    private String endYmd;
}