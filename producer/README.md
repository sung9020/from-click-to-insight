# Producer 모듈

구매 이벤트를 랜덤 생성하여 Kafka에 발행하는 Spring Boot 앱.

## Spring Boot 4.x 주의사항: `spring-boot-kafka` 필수

Spring Boot 4.x부터 auto-configuration이 모듈별로 분리되었다.
`spring-kafka`만 추가하면 `KafkaTemplate` 빈이 자동 생성되지 않는다.

```
// Spring Boot 3.x → 이것만으로 충분했음
implementation("org.springframework.kafka:spring-kafka")

// Spring Boot 4.x → 반드시 spring-boot-kafka도 추가해야 함
implementation("org.springframework.boot:spring-boot-kafka")   // ← Kafka auto-config
implementation("org.springframework.kafka:spring-kafka")        // ← Spring Kafka 라이브러리
```

### 왜?

Spring Boot 3.x에서는 `spring-boot-autoconfigure` 하나에 모든 auto-configuration이 들어있었다.
Spring Boot 4.x에서는 각 기술별로 별도 모듈로 분리되었다:

| 기능 | Spring Boot 3.x | Spring Boot 4.x |
|------|-----------------|-----------------|
| Kafka | `spring-boot-autoconfigure` | `spring-boot-kafka` |
| Jackson | `spring-boot-autoconfigure` | `spring-boot-jackson` |
| Tomcat | `spring-boot-autoconfigure` | `spring-boot-tomcat` |
| WebMvc | `spring-boot-autoconfigure` | `spring-boot-webmvc` |

`spring-boot-starter-web` 같은 starter를 쓰면 관련 모듈이 자동으로 딸려오지만,
`spring-kafka`는 Spring Boot starter가 아니라 Spring Kafka 프로젝트 소속이므로
`spring-boot-kafka`가 자동으로 포함되지 않는다.

### 증상

```
Parameter 0 of constructor in ...EventPublisher required a bean of type
'org.springframework.kafka.core.KafkaTemplate' that could not be found.
```

### 적용 대상

Kafka를 사용하는 **모든 모듈**에 `spring-boot-kafka`를 추가해야 한다:
- `producer/build.gradle.kts`
- `stream-consumer/build.gradle.kts`
- `batch-consumer/build.gradle.kts`
