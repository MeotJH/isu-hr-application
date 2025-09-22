# 발령 관리 시스템 리팩토링 설계서

## 🎯 프로젝트 개요

기존 THRM191(발령이력)과 THRM151(개인조직사항) 테이블의 복잡한 구조를 단일 테이블로 통합하여 
현대적이고 유지보수가 용이한 발령 관리 시스템으로 리팩토링

## 📊 기존 시스템 문제점 분석

### 1. 데이터 중복 및 정합성 문제
- THRM191과 THRM151이 거의 동일한 컬럼 구조
- 동일 정보가 두 테이블에 중복 저장
- 데이터 동기화 복잡성 및 불일치 위험

### 2. 복잡한 조인 쿼리
- 완전한 이력 조회 시 두 테이블 조인 필요
- 성능 저하 및 쿼리 복잡도 증가

### 3. 모호한 개념적 분리
- 두 테이블 모두 실질적으로 기간별 이력 관리
- 논리적 분리의 명확한 근거 부족

## 🏗️ 새로운 아키텍처 설계

### 핵심 설계 원칙
1. **Single Source of Truth**: 단일 테이블로 모든 발령 정보 관리
2. **Modern ID Strategy**: ULID를 활용한 현대적 키 관리
3. **JPA Best Practices**: Spring Boot + JPA 기반 구현
4. **Audit Trail**: 자동화된 감사 로그 관리

## 📋 테이블 설계

### 1. 메인 엔티티: AppointmentHistory

```java
@Entity
@Table(
    name = "appointment_histories",
    indexes = {
        @Index(name = "idx_sabun_effective", columnList = "sabun, effective_date"),
        @Index(name = "idx_current", columnList = "sabun, is_current"),
        @Index(name = "idx_effective_date", columnList = "effective_date")
    }
)
@EntityListeners(AuditingEntityListener.class)
public class AppointmentHistory {
    
    @Id
    @Column(name = "id", length = 26, nullable = false)
    private String id; // ULID
    
    // 비즈니스 식별자
    @Column(name = "enter_cd", nullable = false, length = 20)
    private String enterCode;
    
    @Column(name = "sabun", nullable = false, length = 20)
    private String sabun;
    
    @Column(name = "effective_date", nullable = false, length = 8)
    private String effectiveDate;
    
    // 발령 정보
    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    private AppointmentType appointmentType;
    
    @Column(name = "appointment_reason")
    private String appointmentReason;
    
    // 조직/직급 정보
    @Column(name = "org_cd", nullable = false)
    private String orgCode;
    
    @Column(name = "org_nm")
    private String orgName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EmployeeStatus status;
    
    @Column(name = "position_level")
    private String positionLevel;
    
    @Column(name = "position_name")
    private String positionName;
    
    // 유효기간 관리
    @Column(name = "end_date", length = 8)
    private String endDate = "99991231";
    
    @Builder.Default
    @Column(name = "is_current")
    private Boolean isCurrent = false;
    
    // 감사 정보
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
    }
    
    // Builder pattern constructor
    @Builder
    public AppointmentHistory(String enterCode, String sabun, String effectiveDate,
                            AppointmentType appointmentType, String appointmentReason,
                            String orgCode, String orgName, EmployeeStatus status,
                            String positionLevel, String positionName) {
        this.enterCode = enterCode;
        this.sabun = sabun;
        this.effectiveDate = effectiveDate;
        this.appointmentType = appointmentType;
        this.appointmentReason = appointmentReason;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.status = status;
        this.positionLevel = positionLevel;
        this.positionName = positionName;
    }
    
    protected AppointmentHistory() {}
}
```

### 2. Enum 정의

```java
public enum AppointmentType {
    HIRE("입사", "신규 입사 발령"),
    TRANSFER("전보", "부서 이동"),
    PROMOTION("승진", "직급 승진"),
    RETIRE("퇴직", "퇴직 처리"),
    DISPATCH("파견", "교육/업무 파견"),
    RETURN("복귀", "파견 복귀"),
    SALARY("임금조정", "급여 조정");
    
    private final String displayName;
    private final String description;
    
    AppointmentType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}

public enum EmployeeStatus {
    ACTIVE("재직"),
    RETIRED("퇴직"),
    ON_LEAVE("휴직");
    
    private final String displayName;
    
    EmployeeStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}
```

## 💾 Repository 레이어

```java
@Repository
public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, String> {
    
    // 현재 상태 조회
    Optional<AppointmentHistory> findBySabunAndIsCurrentTrue(String sabun);
    
    // 발령 이력 조회 (최신순)
    List<AppointmentHistory> findBySabunOrderByEffectiveDateDesc(String sabun);
    
    // 특정 시점 상태 조회
    @Query("""
        SELECT a FROM AppointmentHistory a 
        WHERE a.sabun = :sabun 
          AND a.effectiveDate <= :date 
          AND a.endDate >= :date
        """)
    Optional<AppointmentHistory> findByDateRange(@Param("sabun") String sabun, @Param("date") String date);
    
    // 현재 재직자 목록
    List<AppointmentHistory> findByIsCurrentTrueAndStatus(EmployeeStatus status);
    
    // 특정 조직의 현재 직원들
    List<AppointmentHistory> findByOrgCodeAndIsCurrentTrueAndStatus(String orgCode, EmployeeStatus status);
}
```

## ⚡ Service 레이어

```java
@Service
@Transactional
public class AppointmentService {
    
    private final AppointmentHistoryRepository appointmentRepository;
    
    public AppointmentService(AppointmentHistoryRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    
    /**
     * 새로운 발령 등록
     * 기존 트리거 로직을 JPA Service에서 처리
     */
    public AppointmentHistory createAppointment(AppointmentCreateRequest request) {
        String sabun = request.getSabun();
        String effectiveDate = request.getEffectiveDate();
        
        // 1. 기존 현재 상태 레코드를 비활성화하고 종료일 설정
        appointmentRepository.findBySabunAndIsCurrentTrue(sabun)
            .ifPresent(current -> {
                current.setIsCurrent(false);
                current.setEndDate(calculatePreviousDate(effectiveDate));
                current.setUpdatedBy(getCurrentUserId());
                appointmentRepository.save(current);
            });
        
        // 2. 새로운 발령 레코드 생성
        AppointmentHistory newAppointment = AppointmentHistory.builder()
            .enterCode(request.getEnterCode())
            .sabun(sabun)
            .effectiveDate(effectiveDate)
            .appointmentType(request.getAppointmentType())
            .appointmentReason(request.getAppointmentReason())
            .orgCode(request.getOrgCode())
            .orgName(request.getOrgName())
            .status(request.getStatus())
            .positionLevel(request.getPositionLevel())
            .positionName(request.getPositionName())
            .isCurrent(true)
            .createdBy(getCurrentUserId())
            .build();
        
        return appointmentRepository.save(newAppointment);
    }
    
    /**
     * 현재 상태 조회
     */
    @Transactional(readOnly = true)
    public AppointmentHistory getCurrentStatus(String sabun) {
        return appointmentRepository.findBySabunAndIsCurrentTrue(sabun)
            .orElseThrow(() -> new EntityNotFoundException("현재 발령 정보를 찾을 수 없습니다: " + sabun));
    }
    
    /**
     * 발령 이력 조회
     */
    @Transactional(readOnly = true)
    public List<AppointmentHistory> getAppointmentHistory(String sabun) {
        return appointmentRepository.findBySabunOrderByEffectiveDateDesc(sabun);
    }
    
    /**
     * 특정 시점의 상태 조회
     */
    @Transactional(readOnly = true)
    public AppointmentHistory getStatusAtDate(String sabun, String date) {
        return appointmentRepository.findByDateRange(sabun, date)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("해당 시점의 발령 정보를 찾을 수 없습니다. 사번: %s, 날짜: %s", sabun, date)));
    }
    
    private String calculatePreviousDate(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return localDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    
    private String getCurrentUserId() {
        // SecurityContext에서 현재 사용자 ID 조회
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
```

## 📝 DTO 클래스

```java
@Data
@Builder
public class AppointmentCreateRequest {
    private String enterCode;
    private String sabun;
    private String effectiveDate;
    private AppointmentType appointmentType;
    private String appointmentReason;
    private String orgCode;
    private String orgName;
    private EmployeeStatus status;
    private String positionLevel;
    private String positionName;
}

@Data
public class CurrentEmployeeStatusResponse {
    private String id;
    private String sabun;
    private String orgName;
    private String positionName;
    private String statusName;
    private String effectiveDate;
    private String appointmentTypeName;
    
    public static CurrentEmployeeStatusResponse from(AppointmentHistory history) {
        CurrentEmployeeStatusResponse response = new CurrentEmployeeStatusResponse();
        response.setId(history.getId());
        response.setSabun(history.getSabun());
        response.setOrgName(history.getOrgName());
        response.setPositionName(history.getPositionName());
        response.setStatusName(history.getStatus().getDisplayName());
        response.setEffectiveDate(history.getEffectiveDate());
        response.setAppointmentTypeName(history.getAppointmentType().getDisplayName());
        return response;
    }
}

@Data
public class AppointmentHistoryResponse {
    private String id;
    private String effectiveDate;
    private String endDate;
    private String appointmentTypeName;
    private String appointmentReason;
    private String orgName;
    private String positionName;
    private String statusName;
    private Instant createdAt;
    
    public static AppointmentHistoryResponse from(AppointmentHistory history) {
        AppointmentHistoryResponse response = new AppointmentHistoryResponse();
        response.setId(history.getId());
        response.setEffectiveDate(history.getEffectiveDate());
        response.setEndDate(history.getEndDate());
        response.setAppointmentTypeName(history.getAppointmentType().getDisplayName());
        response.setAppointmentReason(history.getAppointmentReason());
        response.setOrgName(history.getOrgName());
        response.setPositionName(history.getPositionName());
        response.setStatusName(history.getStatus().getDisplayName());
        response.setCreatedAt(history.getCreatedAt());
        return response;
    }
}
```

## 🌐 Controller 레이어

```java
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    
    /**
     * 새 발령 등록
     */
    @PostMapping
    public ResponseEntity<CurrentEmployeeStatusResponse> createAppointment(
            @RequestBody @Valid AppointmentCreateRequest request) {
        
        AppointmentHistory appointment = appointmentService.createAppointment(request);
        CurrentEmployeeStatusResponse response = CurrentEmployeeStatusResponse.from(appointment);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * 현재 상태 조회
     */
    @GetMapping("/current/{sabun}")
    public ResponseEntity<CurrentEmployeeStatusResponse> getCurrentStatus(@PathVariable String sabun) {
        AppointmentHistory current = appointmentService.getCurrentStatus(sabun);
        CurrentEmployeeStatusResponse response = CurrentEmployeeStatusResponse.from(current);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 발령 이력 조회
     */
    @GetMapping("/history/{sabun}")
    public ResponseEntity<List<AppointmentHistoryResponse>> getAppointmentHistory(@PathVariable String sabun) {
        List<AppointmentHistory> histories = appointmentService.getAppointmentHistory(sabun);
        List<AppointmentHistoryResponse> responses = histories.stream()
            .map(AppointmentHistoryResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    /**
     * 특정 시점 상태 조회
     */
    @GetMapping("/status/{sabun}")
    public ResponseEntity<CurrentEmployeeStatusResponse> getStatusAtDate(
            @PathVariable String sabun,
            @RequestParam String date) {
        
        AppointmentHistory status = appointmentService.getStatusAtDate(sabun, date);
        CurrentEmployeeStatusResponse response = CurrentEmployeeStatusResponse.from(status);
        return ResponseEntity.ok(response);
    }
}
```

## 🔧 의존성 설정

### build.gradle 추가 의존성

```gradle
dependencies {
    // ULID Generator
    implementation 'com.github.f4b6a3:ulid-creator:5.2.0'
    
    // JPA & Database
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    
    // Database (예시)
    runtimeOnly 'com.oracle.database.jdbc:ojdbc8'
    // 또는 runtimeOnly 'mysql:mysql-connector-java'
    
    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    
    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:oracle-xe'
}
```

### application.yml 설정

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
        show_sql: false
    open-in-view: false
    
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:xe
    username: hr_user
    password: hr_password
    driver-class-name: oracle.jdbc.OracleDriver
    
logging:
  level:
    org.hibernate.SQL: debug
    org.hibernate.type.descriptor.sql.BasicBinder: trace
```

## 🚀 마이그레이션 전략

### 1. 단계적 마이그레이션
1. **Phase 1**: 새 테이블 생성 및 데이터 마이그레이션
2. **Phase 2**: 신규 발령 처리를 새 시스템으로 전환
3. **Phase 3**: 조회 쿼리를 새 시스템으로 전환
4. **Phase 4**: 기존 테이블 백업 후 제거

### 2. 데이터 마이그레이션 스크립트 예시

```sql
-- 기존 THRM151 데이터를 새 테이블로 마이그레이션
INSERT INTO appointment_histories (
    id, enter_cd, sabun, effective_date, appointment_type,
    org_cd, org_nm, status, position_level, position_name,
    end_date, is_current, created_at, created_by
)
SELECT 
    SYS_GUID(), -- 임시로 UUID 생성 (실제로는 ULID 생성 로직 필요)
    ENTER_CD,
    SABUN,
    SDATE,
    'TRANSFER', -- 기본값으로 전보 설정
    ORG_CD,
    COALESCE(ORG_NM, F_COM_GET_ORG_NM(ENTER_CD, ORG_CD)),
    CASE STATUS_CD 
        WHEN 'AA' THEN 'ACTIVE'
        WHEN 'RA' THEN 'RETIRED'
        ELSE 'ACTIVE'
    END,
    JIKWEE_CD,
    JIKWEE_NM,
    EDATE,
    CASE WHEN EDATE = '99991231' THEN 1 ELSE 0 END,
    SYSDATE,
    'MIGRATION'
FROM THRM151
ORDER BY ENTER_CD, SABUN, SDATE;
```

## 📊 성능 최적화

### 1. 인덱스 전략
- **Primary Index**: ULID (자동 시간순 정렬)
- **Business Index**: (sabun, effective_date)
- **Current Status Index**: (sabun, is_current)
- **Date Range Index**: (effective_date)

### 2. 쿼리 최적화
```java
// 현재 상태 조회 (인덱스 활용)
@Query("SELECT a FROM AppointmentHistory a WHERE a.sabun = :sabun AND a.isCurrent = true")
Optional<AppointmentHistory> findCurrentBySabun(@Param("sabun") String sabun);

// 범위 조회 시 페이징 적용
@Query("""
    SELECT a FROM AppointmentHistory a 
    WHERE a.sabun = :sabun 
    ORDER BY a.effectiveDate DESC
    """)
Page<AppointmentHistory> findHistoryWithPaging(@Param("sabun") String sabun, Pageable pageable);
```

## ✅ 장점 요약

### 1. 단순성과 일관성
- 단일 테이블로 모든 발령 정보 관리
- 복합키 제거로 코드 복잡성 감소
- 명확한 비즈니스 로직

### 2. 현대적 기술 스택
- ULID: 시간순 정렬 가능한 고유 식별자
- JPA: 객체 관계 매핑의 표준
- Spring Boot: 현대적 웹 프레임워크

### 3. 성능 향상
- 조인 없는 단일 테이블 조회
- 효율적인 인덱스 설계
- 자동화된 이력 관리

### 4. 유지보수성
- Type-safe한 Enum 활용
- 명시적인 서비스 로직
- 자동화된 감사 추적

## 🧪 테스트 전략

```java
@DataJpaTest
class AppointmentHistoryRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private AppointmentHistoryRepository repository;
    
    @Test
    void 현재_상태_조회_테스트() {
        // given
        AppointmentHistory current = AppointmentHistory.builder()
            .sabun("1005511")
            .effectiveDate("20231201")
            .appointmentType(AppointmentType.TRANSFER)
            .status(EmployeeStatus.ACTIVE)
            .isCurrent(true)
            .build();
        
        entityManager.persistAndFlush(current);
        
        // when
        Optional<AppointmentHistory> result = repository.findBySabunAndIsCurrentTrue("1005511");
        
        // then
        assertThat(result).isPresent();
        assertThat(result.get().getAppointmentType()).isEqualTo(AppointmentType.TRANSFER);
    }
}
```

---

## 📞 문의 및 지원

이 설계서에 대한 문의사항이나 구현 과정에서의 지원이 필요하시면 언제든 연락 바랍니다.

**작성일**: 2025-09-12  
**버전**: v1.0  
**상태**: Draft