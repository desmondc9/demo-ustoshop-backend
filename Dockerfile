# Build stage
FROM gradle:8-jdk20 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle ./build.gradle.kts ./
RUN gradle dependencies --refresh-dependencies --no-daemon
COPY --chown=gradle:gradle . ./
RUN gradle bootJar -x test --no-daemon

# Extract layers stage
FROM eclipse-temurin:20-jre as layers
WORKDIR application
COPY --from=build /home/gradle/src/build/libs/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Final stage: the actual running container
FROM eclipse-temurin:20-jre
WORKDIR app
EXPOSE 8080
COPY --from=layers application/dependencies/ ./
COPY --from=layers application/spring-boot-loader/ ./
#COPY --from=layers application/internal-dependencies/ ./
COPY --from=layers application/snapshot-dependencies/ ./
COPY --from=layers application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
