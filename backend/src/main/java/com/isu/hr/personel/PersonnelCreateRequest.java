package com.isu.hr.personel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonnelCreateRequest {

    private String name;
    private String sabun;
    private String email;
    private String mail;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate hireDate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate retireDate;
}
