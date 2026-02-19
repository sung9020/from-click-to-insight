plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}
dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-kafka")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.parquet:parquet-avro:1.14.0")
    implementation("org.apache.hadoop:hadoop-common:3.3.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}