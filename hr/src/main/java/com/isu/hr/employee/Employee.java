package com.isu.hr.employee;

import com.isu.hr.comm.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hr_employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Employee extends BaseEntity {

    @Column(nullable = false, length = 100, unique = true)
    private String sabun;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "bir_ymd", length = 100)
    private String birYmd;

    @Column(name = "emp_ymd", length = 100)
    private String empYmd;

    @Column(length = 255)
    private String email;

    @Column(length = 100)
    private String address;

    public static Employee createNewEmployee(String sabun, String name, String birYmd, String empYmd, String email, String address) {

        if (sabun == null || sabun.length() == 0) {
            throw new IllegalArgumentException("사번은 필수입니다.");
        }

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        return Employee.builder()
                .sabun(sabun)
                .name(name)
                .birYmd(birYmd)
                .empYmd(empYmd)
                .email(email)
                .address(address)
                .build();
    }


}
