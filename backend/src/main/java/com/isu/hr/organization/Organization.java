package com.isu.hr.organization;

import com.isu.hr.comm.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "org_organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Organization extends BaseEntity {

    @Column(nullable = false, length = 100, unique = true)
    private String code;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "start_ymd", nullable = false, length = 100)
    private String startYmd;

    @Column(name = "end_ymd", length = 100 )
    private String endYmd;


    public static Organization createNewOrganization(String code, String name, String startDate, String endYmd) {

        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("코드는 필수입니다.");
        }

        if (startDate == null || startDate.isEmpty()) {
            throw new IllegalArgumentException("시작일은 필수입니다.");
        }

        OrganizationBuilder builder = Organization.builder()
                .code(code)
                .name(name)
                .startYmd(startDate);

        if(endYmd != null && !endYmd.isEmpty()) {
            builder.endYmd("99991231");
        }else{
            builder.endYmd(endYmd);
        }
        return builder.build();
    }


}
