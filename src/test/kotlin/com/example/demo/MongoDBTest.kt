package com.example.demo

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
abstract class MongoDBTest {

    companion object {
        @Container
        @ServiceConnection
        val mongodb = MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withReuse(true)
    }
}
