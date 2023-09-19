import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    // id("org.graalvm.buildtools.native") version "0.9.26"
    id("com.google.cloud.tools.jib") version "3.3.2"
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
    jacoco
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
}

group = "com.example"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_20
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

extra["springCloudVersion"] = "2022.0.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("org.springframework.boot:spring-boot-starter-security")
    // implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")

    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // arrow
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")
    implementation("io.arrow-kt:arrow-optics:1.2.0")
    ksp("io.arrow-kt:arrow-optics-ksp-plugin:1.2.0")

    // implementation("io.micrometer:micrometer-tracing-bridge-brave")
    // runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    implementation("org.springframework.cloud:spring-cloud-starter")

    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")

    // pdf
    implementation("org.apache.pdfbox:pdfbox:3.0.0")

    // excel
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = JavaVersion.VERSION_20.toString()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<BootJar>("bootJar") {
    layered {
        includeLayerTools = true
    }
}

jib {
    from {
        image = "eclipse-temurin:20-jre"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            // platform {
            //     architecture = "arm64"
            //     os = "linux"
            // }
        }
    }
    to {
        image = "desmondddd/demo-ustoshop-backend"
        tags = setOf("latest", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HH-mm-ss")))
    }
    container {
        mainClass = "com.example.demo.DemoApplicationKt"
    }
}
