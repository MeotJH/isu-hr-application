package com.isu.hr.health;

public interface HealthCheckService {

    /**
     * 서비스 상태정보 조회
     * @return
     */
    HealthCheckResponse getHealth();


    /**
     * 서비스 상태 상세정보 조회
     * @return
     */
    HealthCheckResponse getDetailedHealth();


}
